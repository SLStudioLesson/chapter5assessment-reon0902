package com.taskapp.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.taskapp.dataaccess.UserDataAccess;
import com.taskapp.exception.AppException;
import com.taskapp.logic.TaskLogic;
import com.taskapp.logic.UserLogic;
import com.taskapp.model.User;

public class TaskUI {
    private final BufferedReader reader;

    private final UserLogic userLogic;

    private final TaskLogic taskLogic;

    private User loginUser;

    public TaskUI() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        userLogic = new UserLogic();
        taskLogic = new TaskLogic();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * 
     * @param reader
     * @param userLogic
     * @param taskLogic
     */
    public TaskUI(BufferedReader reader, UserLogic userLogic, TaskLogic taskLogic) {
        this.reader = reader;
        this.userLogic = userLogic;
        this.taskLogic = taskLogic;
    }

    /**
     * メニューを表示し、ユーザーの入力に基づいてアクションを実行します。
     *
     * @see #inputLogin()
     * @see com.taskapp.logic.TaskLogic#showAll(User)
     * @see #selectSubMenu()
     * @see #inputNewInformation()
     */
    public void displayMenu() {
        System.out.println("タスク管理アプリケーションにようこそ!!");
        if (loginUser == null) {
            inputLogin();
        }
        // メインメニュー
        boolean flg = true;
        while (flg) {
            try {
                System.out.println("以下1~3のメニューから好きな選択肢を選んでください。");
                System.out.println("1. タスク一覧, 2. タスク新規登録, 3. ログアウト");
                System.out.print("選択肢：");
                String selectMenu = reader.readLine();

                System.out.println();

                switch (selectMenu) {
                    case "1":
                        taskLogic.showAll(loginUser);
                        selectSubMenu();
                        break;
                    case "2":
                        inputNewInformation();
                        break;
                    case "3":
                        System.out.println("ログアウトしました。");
                        loginUser=null;
                        flg = false;
                        break;
                    default:
                        System.out.println("選択肢が誤っています。1~3の中から選択してください。");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
    if(loginUser==null) {
        inputLogin();
    }
    }

    /**
     * ユーザーからのログイン情報を受け取り、ログイン処理を行います。
     *
     * @see com.taskapp.logic.UserLogic#login(String, String)
     */
    public void inputLogin() {
        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            try {
                System.out.println("メールアドレスを入力してください：");
                String email = reader.readLine();

                System.out.println("パスワードを入力してください：");
                String password = reader.readLine();

                loginUser = userLogic.login(email, password);
                System.out.println("ユーザー名：" + loginUser.getName() + "でログインしました。");

                isLoggedIn = true;

                displayMenu();

            } catch (AppException e) {
                System.out.println("既に登録されているメールアドレス、パスワードを入力してください");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ユーザーからの新規タスク情報を受け取り、新規タスクを登録します。
     *
     * @see #isNumeric(String)
     * @see com.taskapp.logic.TaskLogic#save(int, String, int, User)
     */
    public void inputNewInformation() {
        int taskCode;
        String taskName;
        int usercode;

        while (true) {
            try {
                System.out.print("タスクコードを入力してください：");
                String inputcode = reader.readLine();

                if (!isNumber(inputcode)) {
                    System.out.println("コードは半角の数字で入力してください");
                    continue;
                }
                taskCode = Integer.parseInt(inputcode);
                break;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        while (true) {
            try {
                System.out.println("タスク名を入力してください：");
                taskName = reader.readLine();

                if (taskName.length() <= 10) {
                    break;
                } else {
                    System.out.println("タスク名は10文字以内で入力してください");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        while (true) {
            try {
                System.out.println("担当するユーザーのコードを選択してください：");
                String inputUserCode = reader.readLine();

                if (!isNumber(inputUserCode)) {
                    System.out.println("ユーザーのコードは半角の数字で入力してください");
                    continue;
                }
                usercode = Integer.parseInt(inputUserCode);

                UserDataAccess userDataAccess = new UserDataAccess();
                User users = userDataAccess.findByCode(usercode);

                if (users == null) {
                    System.out.println("存在するユーザーコードを入力してください");
                    continue;
                }
                taskLogic.save(taskCode, taskName, usercode, loginUser);
                System.out.println(taskName + "の登録が完了しました。");
                break;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (AppException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean isNumber(String inputText) {
        return inputText.chars().allMatch(c -> Character.isDigit((char) c));
    }

    /**
     * タスクのステータス変更または削除を選択するサブメニューを表示します。
     *
     * @see #inputChangeInformation()
     * @see #inputDeleteInformation()
     */
    public void selectSubMenu() {
        boolean flg = true;
        while (flg) {
            try {
                System.out.println("以下1~2から好きな選択肢を選んでください。");
                System.out.println("1. タスクのステータス変更, 2. メインメニューに戻る");
                System.out.println("選択肢：");
                String selectSubMenu = reader.readLine();

                switch (selectSubMenu) {
                    case "1":
                        inputChangeInformation();
                        break;
                    case "2":
                        flg = false;
                        break;
                    default:

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void inputChangeInformation(){
        int taskCode;
        int newStatus;
    while(true){
        try{
            System.out.println("ステータスを変更するタスクコードを入力してください：");
            String inputCode=reader.readLine();
            if(!isNumber(inputCode)){
                System.out.println("コードは半角の数字で入力してください");
            continue;
            }
            taskCode=Integer.parseInt(inputCode);
            
            System.out.println("どのステータスに変更するか選択してください。");
            System.out.println("1. 着手中, 2. 完了");
            System.out.println("選択肢：");
        
            String inputStatus=reader.readLine();
            if(!isNumber(inputStatus)){
                System.out.println("ステータスは半角の数字で入力してください");
            continue;
            }
            newStatus=Integer.parseInt(inputStatus);
            if(newStatus!=1&&newStatus!=2){
                System.out.println("ステータスは1・2の中から選択してください");
            }
            taskLogic.changeStatus(taskCode,newStatus,loginUser);
            break;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AppException e) {
            System.out.println(e.getMessage());
        }
    }
}
}
/**
 * ユーザーからのタスクステータス変更情報を受け取り、タスクのステータスを変更します。
 *
 * @see #isNumeric(String)
 * @see com.taskapp.logic.TaskLogic#changeStatus(int, int, User)
 */
// public void inputChangeInformation() {
// }

/**
 * ユーザーからのタスク削除情報を受け取り、タスクを削除します。
 *
 * @see #isNumeric(String)
 * @see com.taskapp.logic.TaskLogic#delete(int)
 */
// public void inputDeleteInformation() {
// }

/**
 * 指定された文字列が数値であるかどうかを判定します。
 * 負の数は判定対象外とする。
 *
 * @param inputText 判定する文字列
 * @return 数値であればtrue、そうでなければfalse
 */
// public boolean isNumeric(String inputText) {
// return false;
// }