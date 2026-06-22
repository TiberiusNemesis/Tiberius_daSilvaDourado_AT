# Music Streaming — Plataforma Antifraude (DDD)

Aplicação de streaming de música (estilo Spotify) com autorização antifraude de
transações, desenvolvida em **Spring Boot** seguindo **Domain-Driven Design**,
princípios **S.O.L.I.D.** e **Clean Code**. API **REST**, persistência com o
**Padrão Repository** sobre banco **H2**.

> Trabalho: `Tiberius_daSilvaDourado_AT`

---

## 1. Visão geral

O sistema processa **operações** — cada requisição representa uma "linha"
processada, e o tipo da operação é decidido a partir do campo `type`:

| `type`                  | Operação                   | Contexto               |
| ----------------------- | -------------------------- | ---------------------- |
| `account`               | Criação de conta           | Listener Account       |
| `credit-card`           | Cadastro de cartão         | Subscription & Billing |
| `subscription`          | Assinatura de um plano     | Subscription & Billing |
| `favorite`              | Favoritar música           | Music Library          |
| `playlist`              | Criar playlist             | Music Library          |
| `add-to-playlist`       | Adicionar faixa à playlist | Music Library          |
| `authorize-transaction` | Autorização de transação   | Subscription & Billing |

### Regras de negócio antifraude

1. **Um plano ativo** — o usuário pode ter somente um plano ativo.
2. **Cartão de crédito válido** — exigido para assinar um plano pago
   (validação estrutural: Luhn + não expirado).
3. **Cartão não ativo** → `cartao-nao-ativo` — nenhuma transação é aceita
   quando o cartão não está ativo.
4. **Alta frequência** → `alta-frequencia-pequeno-intervalo` — não pode haver
   mais de 3 transações em um intervalo de 2 minutos.
5. **Transação duplicada** → `transacao-duplicada` — não pode haver mais de 2
   transações semelhantes (mesmo valor e comerciante) em 2 minutos.

> _Novas regras no futuro:_ cada regra é uma `AuthorizationRule` (Specification)
> registrada numa lista. Adicionar uma regra é adicionar uma classe e registrá-la
> em `BeanConfiguration#authorizationRules` — nada mais muda (**Open/Closed**).

---

- **Listener Account** _(Supporting)_ — ciclo de vida da conta do ouvinte;
  fornecedor _upstream_ da identidade do listener.
- **Music Library** _(Supporting)_ — favoritos e playlists. Relação
  **Customer-Supplier** com Listener Account, protegida por uma
  **Anticorruption Layer** (`AccountDirectory` → `LibraryOwner`): o modelo do
  fornecedor nunca cruza a fronteira.
- **Subscription & Billing** _(Core)_ — assinaturas, cartões e autorização
  antifraude. Em **Partnership** com Listener Account (um assinante é um
  listener; evoluem juntos), também isolado por uma **Anticorruption Layer**
  (`AccountGateway` → `BillingCustomer`).
- **Shared Kernel** — pouquíssimos conceitos universais (`Money`,
  `DomainEvent`, `DomainException`), mantido mínimo de propósito.

Cada Bounded Context é um **módulo Maven** independente. Os módulos de domínio
são **framework-free** (Java puro): apenas o módulo de bootstrap conhece Spring,
JPA e H2.

---

## 2. Estrutura de módulos

```
music-streaming-ddd (pom agregador)
├── shared-kernel        Money, DomainEvent, DomainException
├── listener-account     ListenerAccount (AR), repositório, AccountRegistration
├── music-library        MusicLibrary (AR), Playlist, ACL AccountDirectory
├── billing  (CORE)      Subscriber (AR), CreditCard, Subscription, Transaction,
│                        regras antifraude, TransactionAuthorizationService, ACL
└── streaming-app        Spring Boot: REST, wiring, adaptadores H2/JPA e ACL
```

Cada contexto segue camadas `domain` (model, repository, service) →
`application` (casos de uso) → e, no bootstrap, `infrastructure` (persistência)
e `rest` (interface).

---

## 3. Como executar

Pré-requisitos: **JDK 21+** e **Maven 3.9+**.

```bash
# compilar e instalar os módulos (uma vez)
mvn -DskipTests install

# rodar a aplicação
mvn -pl streaming-app spring-boot:run
```

A API sobe em `http://localhost:8080`. Console do H2 em
`http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:streaming`, user `sa`).

---

### Comportamento antifraude (verificado)

| Cenário                                         | Resultado                                        |
| ----------------------------------------------- | ------------------------------------------------ |
| Assinar plano pago sem cartão válido            | `422` — sem cartão válido                        |
| Assinar com plano já ativo                      | `422` — apenas um plano ativo                    |
| Transação com cartão inativo                    | `rejected` → `cartao-nao-ativo`                  |
| 4ª transação em 2 min                           | `rejected` → `alta-frequencia-pequeno-intervalo` |
| 3ª transação igual (valor+comerciante) em 2 min | `rejected` → `transacao-duplicada`               |
| Transação limpa                                 | `approved` (+ `transactionId`)                   |

---

## 4. Notas de modelagem

- O número de cartão `4242 4242 4242 4242` é Luhn-válido (cartão de teste).
- A janela antifraude usa o `occurredAt` da própria transação, permitindo
  reproduzir os cenários de forma determinística.
- Validade (`isValid`) e atividade (`isActive`) do cartão são conceitos
  **distintos** no modelo, refletindo as duas regras correspondentes.
