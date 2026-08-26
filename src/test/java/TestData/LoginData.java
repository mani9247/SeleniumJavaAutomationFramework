package TestData;

public class LoginData {
    private final String username;
    private final String password;
    private final String expectedResult;

    public LoginData(
            String username,
            String password,
            String expectedResult) {

        this.username = username;
        this.password = password;
        this.expectedResult = expectedResult;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    @Override
    public String toString() {

        return "Username=" + username
                + ", ExpectedResult=" + expectedResult;
    }

}
