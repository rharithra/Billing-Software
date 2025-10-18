package com.billingsoftware;

import com.billingsoftware.dao.CustomerDAO;
import com.billingsoftware.dao.UserDAO;
import com.billingsoftware.model.User;
import com.billingsoftware.ui.LoginScreen;
import com.billingsoftware.util.EmbeddedDBTest;

import javax.swing.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new LoginScreen();
        });



    }

    }
