com.mybank.app
│
├── Main.java
│
├── config/
│   ├── JPAConfig.java
│   └── ApplicationContext.java
│
├── entity/                    ← JPA ENTITIES (classes)
│   ├── UserEntity.java
│   ├── AccountEntity.java
│   ├── TransactionEntity.java
│   └── LoginAuditEntity.java
│
├── repository/
│   ├── UserRepository.java
│   ├── AccountRepository.java
│   ├── TransactionRepository.java
│   └── LoginAuditRepository.java
│
├── service/
│   ├── AuthService.java
│   ├── AccountService.java
│   ├── TransactionService.java
│   └── BankService.java        ← orchestration layer
│
├── controller/
│   ├── AuthController.java
│   ├── AccountController.java
│   └── TransactionController.java
│
├── record/                     ← NO BOILERPLATE
│   ├── LoginRequest.java
│   ├── LoginResult.java
│   ├── CreateAccountCommand.java
│   ├── TransferCommand.java
│   ├── AccountView.java
│   ├── TransactionView.java
│
├── constant/                   ← replaces enums
│   ├── AccountConstants.java
│   ├── TransactionConstants.java
│   └── UserConstants.java
│
├── exception/
│   ├── BankingException.java
│   ├── AuthenticationFailedException.java
│   ├── InsufficientBalanceException.java
│   └── AccountInactiveException.java
│
├── util/
│   ├── InputUtil.java
│   ├── PasswordHasher.java
│   ├── AccountNumberGenerator.java
│   └── DateTimeUtil.java
│
└── session/
└── UserSession.java
