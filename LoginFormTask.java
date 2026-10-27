import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

class User {
    private String username;
    private String password;
    private String role;

    User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}

class WelcomePage extends Frame {

    WelcomePage(User user) {
        super("Welcome");
        setLayout(new BorderLayout());

        Label welcomeLabel = new Label("Hello, " + user.getUsername(), Label.CENTER);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));

        Label roleLabel = new Label("Role: " + user.getRole(), Label.CENTER);
        roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));

        Panel panel = new Panel(new GridLayout(2, 1, 10, 10));
        panel.add(welcomeLabel);
        panel.add(roleLabel);

        add(panel, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setSize(400, 200);
        setLocationRelativeTo(null);
    }
}

public class LoginFormTask extends Frame {

    
    private List<User> users;
    private int failedAttempts = 0; 
    private static final int MAX_ATTEMPTS = 3;

    public LoginFormTask() {
        super("Login");
        loadUsers();

        setLayout(new GridBagLayout());
        Panel content = new Panel(new BorderLayout(10, 15));

        Label title = new Label("Welcome back", Label.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        content.add(title, BorderLayout.NORTH);

        Panel form = new Panel(new GridLayout(3, 2, 8, 8));
        form.add(new Label("Username:", Label.RIGHT));
        TextField username = new TextField(18);
        form.add(username);

        form.add(new Label("Password:", Label.RIGHT));
        TextField password = new TextField(18);
        password.setEchoChar('*');
        form.add(password);

        form.add(new Label("Role:", Label.RIGHT));
        Choice role = new Choice();
        role.add("Student");
        role.add("TA");
        role.add("Professor");
        form.add(role);

        content.add(form, BorderLayout.CENTER);

        Panel buttons = new Panel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        Button cancel = new Button("Cancel");
        Button login = new Button("Login");
        buttons.add(cancel);
        buttons.add(login);
        content.add(buttons, BorderLayout.SOUTH);

        add(content);

        login.addActionListener(e -> {
            if (username.getText().trim().isEmpty() || password.getText().isEmpty()) {
                showMessage("Error", "Please fill in all fields!");
                return;
            }

            
            User user = findUser(username.getText().trim());
            
            if (user == null) {
                showMessage("Error", "User not found!");
                failedAttempts++;
                checkFailedAttempts(login);
                return;
            }

            
            if (!user.getPassword().equals(password.getText())) {
                showMessage("Error", "Incorrect password!");
                failedAttempts++;
                checkFailedAttempts(login);
                return;
            }

            
            if (!user.getRole().equals(role.getSelectedItem())) {
                showMessage("Error", "Invalid role selected!");
                return;
            }

            
            failedAttempts = 0; 
            showMessage("Success", "Login successful! Welcome " + user.getUsername());
            new WelcomePage(user).setVisible(true);
            dispose(); 
        });

        
        cancel.addActionListener(e -> {
            dispose();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        
        setSize(420, 340);
        setLocationRelativeTo(null);
    }

    
    private void loadUsers() {
        users = new ArrayList<>();
        users.add(new User("student1", "pass123", "Student"));
        users.add(new User("ta1", "ta123", "TA"));
        users.add(new User("prof1", "prof123", "Professor"));
    }

    
    private User findUser(String name) {
        for (User user : users) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }

    
    private void showMessage(String title, String message) {
        Dialog dialog = new Dialog(this, title, true);
        dialog.setLayout(new BorderLayout());
        
        Label msgLabel = new Label(message, Label.CENTER);
        msgLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dialog.add(msgLabel, BorderLayout.CENTER);
        
        Button okButton = new Button("OK");
        okButton.addActionListener(e -> dialog.dispose());
        Panel buttonPanel = new Panel();
        buttonPanel.add(okButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setSize(300, 120);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

   
    private void checkFailedAttempts(Button loginButton) {
        if (failedAttempts >= MAX_ATTEMPTS) {
            loginButton.setEnabled(false);
            showMessage("Account Locked", "Too many failed attempts! Login button is disabled.");
        }
    }

    public static void main(String[] args) {
        new LoginFormTask().setVisible(true);
    }
}
