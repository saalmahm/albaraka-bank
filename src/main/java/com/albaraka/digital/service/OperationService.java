@Transactional
public Operation createClientOperation(OperationRequest request) {

    Account source = accountService.getCurrentUserAccount();

    BigDecimal amount = request.getAmount();
    if (amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("Le montant doit être strictement positif");
    }

    OperationType type = request.getType();

    Account destination = null;
    if (type == OperationType.TRANSFER) {
        if (request.getDestinationAccountNumber() == null || request.getDestinationAccountNumber().isBlank()) {
            throw new IllegalArgumentException("Le numéro de compte destinataire est obligatoire pour un virement");
        }
        destination = accountRepository.findByAccountNumber(request.getDestinationAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Compte destinataire introuvable"));

        if (destination.getId().equals(source.getId())) {
            throw new IllegalArgumentException("Le compte source et le compte destinataire doivent être différents");
        }
    }

    /* 
       CAS A : montant ≤ 10 000
      */
    if (amount.compareTo(THRESHOLD) <= 0) {

        // Vérifier solde suffisant pour retraits et virements
        if (type == OperationType.WITHDRAWAL || type == OperationType.TRANSFER) {
            if (source.getBalance().compareTo(amount) < 0) {
                throw new IllegalArgumentException("Solde insuffisant pour effectuer cette opération");
            }
        }

        Operation operation = Operation.builder()
                .type(type)
                .amount(amount)
                .status(OperationStatus.VALIDATED)
                .accountSource(source)
                .accountDestination(destination)
                .createdAt(LocalDateTime.now())
                .executedAt(LocalDateTime.now())
                .build();

        // Mise à jour des soldes
        switch (type) {
            case DEPOSIT -> source.setBalance(source.getBalance().add(amount));
            case WITHDRAWAL -> source.setBalance(source.getBalance().subtract(amount));
            case TRANSFER -> {
                source.setBalance(source.getBalance().subtract(amount));
                destination.setBalance(destination.getBalance().add(amount));
                accountRepository.save(destination);
            }
        }

        accountRepository.save(source);
        return operationRepository.save(operation);
    }

    /*
       CAS B : montant > 10 000
     */
    Operation operation = Operation.builder()
            .type(type)
            .amount(amount)
            .status(OperationStatus.PENDING)
            .accountSource(source)
            .accountDestination(destination)
            .createdAt(LocalDateTime.now())
            .build();

    return operationRepository.save(operation);
}
