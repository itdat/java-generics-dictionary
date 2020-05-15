package com.ntdat;

import java.util.Scanner;

public class UserInterface {
    public static final String LINE_BREAK = "-----------------------------------------------";

    public int GetMenuOption(String[] options) {
        System.out.println(LINE_BREAK);
        for (int i = 0; i < options.length; i++) {
            System.out.println(i + 1 + ". " + options[i]);
        }
        System.out.println(LINE_BREAK);

        int option;
        Scanner sc = new Scanner(System.in);
        do {
            System.out.print("Nhap lua chon: ");
            option =sc.nextInt();
        } while (option < 1 || option > options.length);
        return option;
    }

    public void ShowAlert(String msg) {
        System.out.println(LINE_BREAK);
        System.out.println(msg);
    }

    public String ShowPrompt(String msg) {
        Scanner sc = new Scanner(System.in);
        String val;
        System.out.println(LINE_BREAK);
        do {
            System.out.print(msg);
            val = sc.nextLine();
        } while (val.isEmpty());
        return val;
    }

    public boolean ShowConfirm(String msg) {
        System.out.println(LINE_BREAK);
        System.out.print(msg + " (Y/N) ");
        Scanner sc = new Scanner(System.in);
        char comfirm = sc.next().charAt(0);
        if (Character.toUpperCase(comfirm) == 'Y') return true;
        else return false;
    }

    public void PauseProgram() {
        System.out.println(LINE_BREAK);
        System.out.print("Bam phim bat ky de quay lai.");
        Scanner sc = new Scanner(System.in);
        sc.nextLine();
    }
}
