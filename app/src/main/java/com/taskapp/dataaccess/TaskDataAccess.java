package com.taskapp.dataaccess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.taskapp.model.Task;
import com.taskapp.model.User;

public class TaskDataAccess {

    private final String filePath;

    private final UserDataAccess userDataAccess;

    public TaskDataAccess() {
        filePath = "app/src/main/resources/tasks.csv";
        userDataAccess = new UserDataAccess();
    }

    /**
     * 自動採点用に必要なコンストラクタのため、皆さんはこのコンストラクタを利用・削除はしないでください
     * 
     * @param filePath
     * @param userDataAccess
     */
    public TaskDataAccess(String filePath, UserDataAccess userDataAccess) {
        this.filePath = filePath;
        this.userDataAccess = userDataAccess;
    }

    /**
     * CSVから全てのタスクデータを取得します。
     *
     * @see com.taskapp.dataaccess.UserDataAccess#findByCode(int)
     * @return タスクのリスト
     */
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                int taskcode = Integer.parseInt(values[0]);
                String taskName = values[1];
                int status = Integer.parseInt(values[2]);
                int usercode = Integer.parseInt(values[3]);

                User user = userDataAccess.findByCode(usercode);
                Task task = new Task(taskcode, taskName, status, user);

                tasks.add(task);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    /**
     * タスクをCSVに保存します。
     * @param task 保存するタスク
     */
    public void save(Task task) {
        try (BufferedWriter writer=new BufferedWriter(new FileWriter(filePath,true))) {
            writer.write(task.getCode()+","+task.getName()+","+task.getStatus()+","+task.getRepUser().getCode());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * コードを基にタスクデータを1件取得します。
     * 
     * @param code 取得するタスクのコード
     * @return 取得したタスク
     */
     public Task findByCode(int code) {
     Task task=null;
        try (BufferedReader reader=new BufferedReader(new FileReader(filePath))) {
            String line;
            reader.readLine();
        while((line=reader.readLine())!=null){
            String[] values=line.split(",");
            int taskCode=Integer.parseInt(values[0]);
            String name=values[1];
            int status=Integer.parseInt(values[2]);
            int repUserCode=Integer.parseInt(values[3]);
            User repUser=userDataAccess.findByCode(repUserCode);
            if(taskCode==code){
            task=new Task(taskCode,name,status,repUser);
            break;
        }
    }
     } catch (IOException e) {
     e.printStackTrace();
     }
     return task;
     }

    /**
     * タスクデータを更新します。
     * 
     * @param updateTask 更新するタスク
     */
     public void update(Task updateTask) {
     List<Task> task=findAll();
        for(int i=0;i<task.size();i++){
            Task tasks=task.get(i);
        if(tasks.getCode()==updateTask.getCode()){
            task.set(i,updateTask);
            break;
        }
        
        }
        // コメント：FileWriterインスタンス生成時に第二引数をtrueにすると、上書きモードになってしまいます。
        // try (BufferedWriter writer=new BufferedWriter(new FileWriter(filePath,true))) {
        try (BufferedWriter writer=new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Code,Name,Status,Rep_User_Code");
            writer.write("\n");
            for(Task task2:task){
                String line=createLine(task2);
            writer.write(line);
            writer.newLine();
            }
            }catch (IOException e) {
            e.printStackTrace();
            }
        }
    public String createLine(Task task){
        // コメント：task.getRepUser()はオブジェクトなので文字化けします。getCode()を実行しましょう
        // return task.getCode()+","+task.getName()+","+task.getStatus()+","+task.getRepUser();
        return task.getCode()+","+task.getName()+","+task.getStatus()+","+task.getRepUser().getCode();
    }

    /**
     * コードを基にタスクデータを削除します。
     * 
     * @param code 削除するタスクのコード
     */
     
     
     
     
     
     
        
        
        
    // public void delete(int code) {
    // try () {

    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    /**
     * タスクデータをCSVに書き込むためのフォーマットを作成します。
     * 
     * @param task フォーマットを作成するタスク
     * @return CSVに書き込むためのフォーマット文字列
     */
    // private String createLine(Task task) {
    // }
}