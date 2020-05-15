package com.ntdat;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        String[] mainMenu = {"Tu dien Anh - Viet", "Tu dien Viet - Anh", "Tu yeu thich",
                "Thong ke tra cuu", "Thoat chuong trinh"};

        UserInterface userInterface = new UserInterface();
        Dictionary dictionary = new Dictionary();

        while (true) {
            userInterface.ShowAlert("UNG DUNG TU DIEN - MENU CHINH");
            int option = userInterface.GetMenuOption(mainMenu);

            switch (option) {
                case 1:
                    dictionary.setType(Dictionary.EN_TO_VI);
                    dictionary.LoadDictionary();
                    dictionary.ShowFeatures();
                    break;
                case 2:
                    dictionary.setType(Dictionary.VI_TO_EN);
                    dictionary.LoadDictionary();
                    dictionary.ShowFeatures();
                    break;
                case 3:
                    userInterface.ShowAlert("DANH SACH TU YEU THICH");
                    String[] sortOptions = {"Sap xep tu A-Z", "Sap xep tu Z-A"};
                    int sortOption = userInterface.GetMenuOption(sortOptions);

                    switch (sortOption) {
                        case 1:
                            userInterface.ShowAlert("Danh sach tu tieng Anh:");
                            dictionary.setType(Dictionary.EN_TO_VI);
                            dictionary.LoadFavouriteList();
                            dictionary.ShowFavouriteList(Dictionary.SORT_A_Z);
                            userInterface.ShowAlert("Danh sach tu tieng Viet:");
                            dictionary.setType(Dictionary.VI_TO_EN);
                            dictionary.LoadFavouriteList();
                            dictionary.ShowFavouriteList(Dictionary.SORT_A_Z);
                            break;
                        case 2:
                            userInterface.ShowAlert("Danh sach tu tieng Anh:");
                            dictionary.setType(Dictionary.EN_TO_VI);
                            dictionary.LoadFavouriteList();
                            dictionary.ShowFavouriteList(Dictionary.SORT_Z_A);
                            userInterface.ShowAlert("Danh sach tu tieng Viet:");
                            dictionary.setType(Dictionary.VI_TO_EN);
                            dictionary.LoadFavouriteList();
                            dictionary.ShowFavouriteList(Dictionary.SORT_Z_A);
                            break;
                    }
                    userInterface.PauseProgram();
                    break;
                case 4:
                    userInterface.ShowAlert("THONG KE TRA CUU");
                    String strDate1, strDate2;

                    do {
                        strDate1 = userInterface.ShowPrompt("Nhap vao ngay 1 (dd/MM/yyyy): ");
                    } while (!DateValidator.Validate(strDate1));

                    do {
                        strDate2 = userInterface.ShowPrompt("Nhap vao ngay 2 (dd/MM/yyyy): ");
                    } while (!DateValidator.Validate(strDate2));

                    LocalDate d1 = DateValidator.toLocalDate(strDate1);
                    LocalDate d2 = DateValidator.toLocalDate(strDate2);

                    dictionary.setType(Dictionary.EN_TO_VI);
                    dictionary.LoadHistory();
                    userInterface.ShowAlert("Danh sach tu tieng Anh:");
                    dictionary.ShowHistory(d1, d2);

                    dictionary.setType(Dictionary.VI_TO_EN);
                    dictionary.LoadHistory();
                    userInterface.ShowAlert("Danh sach tu tieng Viet:");
                    dictionary.ShowHistory(d1, d2);

                    userInterface.PauseProgram();
                    break;
                case 5:
                    userInterface.ShowAlert("Da thoat chuong trinh.");
                    System.exit(0);
            }
        }
    }
}
