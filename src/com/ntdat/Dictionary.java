package com.ntdat;

import java.time.LocalDate;
import java.util.*;

public class Dictionary {
    public static final int EN_TO_VI = 1;
    public static final int VI_TO_EN = 2;
    private static final String EV_MENU_TITLE = "TU DIEN ANH -> VIET";
    private static final String VE_MENU_TITLE = "TU DIEN VIET -> ANH";
    public static final int SORT_A_Z = 1;
    public static final int SORT_Z_A = 2;

    private int type;
    private HashMap<String, String> dictionaryMap;
    private List<String> favouriteList = new ArrayList<>();
    private List<HistoryRecord> historyRecordList = new ArrayList<>();

    public void setType(int type) {
        if (type != EN_TO_VI && type != VI_TO_EN) {
            type = EN_TO_VI;
        }
        this.type = type;
    }

    public void LoadDictionary() {
        this.dictionaryMap = DictionaryData.LoadDictionary(this.type);
    }

    public void LoadFavouriteList() {
        this.favouriteList = DictionaryData.LoadFavouriteList(this.type);
    }

    public void LoadHistory() {
        this.historyRecordList = DictionaryData.LoadHistory(this.type);
    }

    public String LookUp(String word) {
        this.LoadHistory();
        this.PushWordToHistory(word);
        DictionaryData.StoreHistory(this.type, this.historyRecordList);
        return dictionaryMap.get(word);
    }

    private void PushWordToHistory(String word) {
        LocalDate today = LocalDate.now();
        if (this.historyRecordList.size() > 0) {
            // Get last history record if possible
            HistoryRecord lastHistoryRecord = this.historyRecordList.get(this.historyRecordList.size() - 1);
            LocalDate lastDay = lastHistoryRecord.getDate();

            if (lastDay.compareTo(today) == 0) {
                HashMap<String, Integer> hashMap = lastHistoryRecord.getRecord();
                if (hashMap.containsKey(word)) {
                    hashMap.replace(word, hashMap.get(word) + 1);
                } else {
                    hashMap.put(word, 1);
                }
                return;
            }
        }
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put(word, 1);
        HistoryRecord historyRecord = new HistoryRecord(today, hashMap);
        this.historyRecordList.add(historyRecord);
    }

    public boolean AddRecord(String word, String meaning) {
        if (!this.dictionaryMap.containsKey(word)) {
            this.dictionaryMap.put(word, meaning);
            DictionaryData.StoreDictionary(this.type, this.dictionaryMap);
            return true;
        } else  {
            return false;
        }
    }

    public boolean RemoveRecord(String word) {
        if (this.dictionaryMap.containsKey(word)) {
            this.dictionaryMap.remove(word);
            DictionaryData.StoreDictionary(this.type, this.dictionaryMap);
            return true;
        } else  {
            return false;
        }
    }

    public void AddToFavouriteList(String word) {
        favouriteList.add(word);
        DictionaryData.StoreFavouriteList(this.type, this.favouriteList);
    }

    public void RemoveFromFavouriteList(String word) {
        favouriteList.remove(word);
        DictionaryData.StoreFavouriteList(this.type, this.favouriteList);
    }

    public void ShowFeatures() {
        UserInterface userInterface = new UserInterface();
        String[] dictionaryMenu = {"Tra cuu", "Them tu", "Xoa tu", "Quay lai menu chinh"};
        boolean backToMainMenu = false;

        while (!backToMainMenu) {
            String title;
            if (this.type == EN_TO_VI) {
                title = EV_MENU_TITLE;
            } else {
                title = VE_MENU_TITLE;
            }
            userInterface.ShowAlert(title);

            int dictionaryOption = userInterface.GetMenuOption(dictionaryMenu);
            String word, meaning;

            switch (dictionaryOption) {
                case 1: // Tra cuu
                    while (true) {
                        word = userInterface.ShowPrompt("Nhap tu can tra cuu: ");
                        meaning = this.LookUp(word);

                        if (meaning != null) {
                            System.out.println(meaning);
                            this.LoadFavouriteList();
                            if (!this.favouriteList.contains(word)) {
                                boolean like = userInterface.ShowConfirm("Ban co muon them [" + word + "] vao danh sach yeu thich?");
                                if (like) {
                                    this.AddToFavouriteList(word);
                                    userInterface.ShowAlert("[!] Da them [" + word + "] vao danh sach yeu thich.");
                                }
                            } else {
                                boolean dislike = userInterface.ShowConfirm("Ban co muon xoa [" + word + "] khoi danh sach yeu thich?");
                                if (dislike) {
                                    this.RemoveFromFavouriteList(word);
                                    userInterface.ShowAlert("[!] Da xoa [" + word + "] khoi danh sach yeu thich.");
                                }
                            }
                        } else {
                            userInterface.ShowAlert("[!] Khong tim thay ket qua.");
                        }

                        boolean continueLookUp = userInterface.ShowConfirm("Ban co muon tiep tuc tra cuu?");
                        if (!continueLookUp) {
                            break;
                        }
                    }
                    break;
                case 2: // Them tu
                    word = userInterface.ShowPrompt("Nhap tu can them: ");
                    meaning = userInterface.ShowPrompt("Nhap nghia cua tu: ");
                    if (this.AddRecord(word, meaning)) {
                        if (DictionaryData.StoreDictionary(this.type, this.dictionaryMap)) {
                            userInterface.ShowAlert("[!] Da them thanh cong tu [" + word + "].");
                        } else {
                            userInterface.ShowAlert("[!] Them that bai. Xay ra loi luu du lieu.");
                        }
                    } else {
                        userInterface.ShowAlert("[!] Them that bai. [" + word + "] da ton tai.");
                    }
                    userInterface.PauseProgram();
                    break;
                case 3: // Xoa tu
                    word = userInterface.ShowPrompt("Nhap tu can xoa: ");
                    if (this.RemoveRecord(word)) {
                        if (DictionaryData.StoreDictionary(this.type, this.dictionaryMap)) {
                            userInterface.ShowAlert("[!] Da xoa thanh cong tu [" + word + "].");
                        }
                    } else {
                        userInterface.ShowAlert("[!] Xoa that bai. [" + word + "] khong ton tai.");
                    }
                    userInterface.PauseProgram();
                    break;
                case 4:
                    backToMainMenu = true;
                    break;
            }
        }
    }

    public void ShowFavouriteList(int sortOption) {
        if (sortOption == SORT_A_Z) {
            Collections.sort(this.favouriteList);
        } else {
            Collections.sort(this.favouriteList, Collections.reverseOrder());
        }

        if (favouriteList.isEmpty()) {
            System.out.println("[!] Khong tim thay du lieu.");
        }

        for (String word : favouriteList) {
            System.out.println("- " + word);
        }
    }

    public void ShowHistory(LocalDate d1, LocalDate d2) {
        HashMap<String, Integer> hashMap = new HashMap<>();

        for (HistoryRecord hr : historyRecordList) {
            if (hr.getDate().isAfter(d1) && hr.getDate().isBefore(d2)) {
                hr.getRecord().forEach((key, value) -> {
                    if (!hashMap.containsKey(key)) {
                        hashMap.put(key, value);
                    } else {
                        hashMap.put(key, hashMap.get(key) + value);
                    }
                });
            }
        }

        if (hashMap.isEmpty()) {
            UserInterface userInterface = new UserInterface();
            System.out.println("[!] Khong tim thay du lieu.");
            return;
        }

        hashMap.forEach((key, value) -> {
            System.out.println("- " + key + ": " + value + " lan");
        });
    }
}
