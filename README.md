# Bank System

Java console application, simulating performing basic operations in a bank system.

## Functionalities

### Register

Registers new user in the system.

```
register <username> <password> <repeat_password> <email> <user_status>
```

### Login

Logs a registered user in the system.

```
login <username> <password>
```

### Delete user profile

Deletes current user's profile.

```
delete-profile
```

### Get user details

Displays user's personal details.

```
get-user <password>*
```

*If logged as admin or clerk, can get other user details by specifying the username.

### Send message

Allows the current user to send another user a message.

```
send-message <username> <message>
```

### Read new messages

Displays to the current user the new messages in inbox.

```
read-new-messages
```

### Logout

Logs user out of the system

```
logout
```

### Create account

Allows the current user to create a new bank account.

```
create-account <account_type> <initial_money>
```

### List accounts

Displays all bank accounts, belonging to the current user.

```
list-accounts
```

### Transfer money

Allows the current user to transfer money from one of his accounts to another account.

```
transfer <iban_from> <iban_to> <money>
```

### Cash money in

Allows the current user to transfer money into an account.

```
cash-in <money> <iban>
```

### Withdraw money

Allows the current user to take money from one of his accounts.

```
withdraw <iban> <money>
```

### Set account interest rate

Allows an admin or a clerk to change an account's interest rate.

```
set-rate <iban> <interest_rate>
```

### Calculate best savings

Displays the money the current user can save and put into a savings account based on the incomes and outcomes in the previous period (if there's one, otherwise the current) and the money now in his accounts.

```
calculate-best-savings
```

### Move period

Allows an admin or a clerk to move all accounts in the system to the next period.

```
move-period
```

## Specifics

### User status

* CLIENT
* CLERK
* ADMIN

### Password

Passwords are required to have minimum 8 characters, of which at least 1 lowercase letter, 1 uppercase letter and 1 digit

### Account type

#### Checking Account

* Type - checkingAccount
* Description - Secure and easy access to your money for your daily transactional needs
* Minimum opening deposit - 100
* Period service fee - 1
* Interest rate - 0.1

#### Savings Account

* Type - savingsAccount
* Description - Accumulate interest on funds you have saved for future needs.
* Minimum opening deposit - 150
* Period service fee - 2
* Interest rate - 0.05

#### Certificate of Deposit

* Type - certificateOfDeposit
* Description - Invest your money at a set interest rate for a pre-set period of time
* Minimum opening deposit - 700
* Period service fee - 3
* Interest rate - 0.1

#### Money Market Account

* Type - moneyMarketAccount
* Description - Accumulate interest on funds you have saved for future needs by maintaining a higher balance to avoid a monthly service fee.
* Minimum opening deposit - 250
* Period service fee - 0
* Interest rate - 0.1

#### Individual Retirement Account

##### Traditional IRA

* Type - traditionalIRA
* Description - Save independently for your retirement. Contributions are tax-deductible.
* Minimum opening deposit - 200
* Period service fee - 2
* Interest rate - 0.1
* Contribution limit - 300

##### Roth IRA

* Type - rothIRA
* Description - Save independently for your retirement. Funds can be withdrawn tax-free in many situations.
* Minimum opening deposit - 200
* Period service fee - 2
* Interest rate - 0.1
* Contribution limit - 400

### Files

* users.txt - users database
* accounts.txt - accounts database 

```
The 'resources' directory and the two files are automatically generated the first time the application is started.
  
Initially the database contains 2 registered users:
 - Username: admin  Password: adminAdmin5
 - Username: clerk  Password: clerk12Clerk
```

## Structure

```
src
- bg/sofia/uni/fmi/mjt/project/
   - BankClient.java
   - BankServer.java 
   - DatabasesConstants.java
   - accounts/
   |   - Account.java
   |   - AccountProfile.java
   |   - AccountsDatabase.java
   |   - Period.java
   |   - Transaction.java
   |   - types/
   |      - AccountType.java
   |      - AccountTypeFactory.java
   |      - CertificateOfDeposit.java
   |      - CheckingAccount.java
   |      - IndividualRetirementAccount.java
   |      - MoneyMarketAccount.java
   |      - RothIRA.java
   |      - SavingsAccount.java
   |      - TraditionalIRA.java
   - actions/
   |   - Action.java
   |   - ActionFactory.java
   |   - ActionsConstants.java
   |   - ClientActionFactory.java
   |   - ServerActionFactory.java
   |   - account/
   |   |  - AccountAction.java
   |   |  - CalculateBestSavingsAction.java
   |   |  - CashInMoneyAction.java
   |   |  - CreateAccountAction.java
   |   |  - DisplayAccountsAction.java
   |   |  - MovePeriodAction.java
   |   |  - SetAccountInterestRateAction.java
   |   |  - TransferMoneyAction.java
   |   |  - WithdrawMoneyAction.java
   |   - user/
   |      - DeleteUserAction.java
   |      - GetUserAction.java
   |      - LoginAction.java
   |      - LogoutAction.java
   |      - ReadMessagesAction.java
   |      - RegisterAction.java
   |      - SendMessageAction.java
   |		- SocketDisconnect.java
   |      - UserAction.java
   - exceptions/
   |   - InvalidFieldException.java
   |   - NoSuchAccountException.java
   |   - NoSuchUserException.java
   |   - NotLoggedException.java
   |   - UserAlreadyLoggedException.java
   |   - ViolationException.java
   - threads/
   |   - ClientRunnable.java
   |   - ServerRunnable.java
   - users/
       - Status.java
       - User.java
       - UserProfile.java
       - UsersDatabase.java
test
- bg/sofia/uni/fmi/mjt/project/
   - accounts/
   |   - AccountProfileTest.java
   |   - AccountsDatabaseTest.java
   |   - AccountTest.java
   - actions/
   |   - ClientActionFactoryTest.java
   |   - ServerActionFactoryTest.java
   |   - account/
   |   |  - CalculateBestSavingsActionTest.java
   |   |  - CashInMoneyActionTest.java
   |   |  - CreateAccountActionTest.java
   |   |  - DisplayAccountsActionTest.java
   |   |  - MovePeriodActionTest.java
   |   |  - SetAccountInterestRateActionTest.java
   |   |  - TransferMoneyActionTest.java
   |   |  - WithdrawMoneyActionTest.java
   |   - user/
   |      - DeleteUserActionTest.java
   |      - GetUserActionTest.java
   |      - LoginActionTest.java
   |      - LogoutActionTest.java
   |      - ReadMessagesActionTest.java
   |      - RegisterActionTest.java
   |      - SendMessageActionTest.java
   - users/
       - UsersDatabaseTest.java
lib
- byte-buddy-1.9.0.jar
- byte-buddy-agent-1.9.0.jar
- gson-2.8.5.jar
- iban4j-3.2.1.jar
- mockito-core-2.23.4.jar
- objenesis-2.6.jar
resources
- accounts.txt
- users.txt
```