package com.ls.g4.view;

import com.ls.g4.po.Message;
import com.ls.g4.util.DateUtil;
import org.fusesource.jansi.AnsiConsole;

import java.util.Scanner;

public class View {

  private Scanner scanner;

  public View() {
    scanner = new Scanner(System.in);
    AnsiConsole.systemInstall();
  }

  public String input() {
    return scanner.nextLine();
  }

  public void show(String show) {
    System.out.println(show);
  }


  public void showMessage(Message message,boolean isOwner){
    AnsiConsole.system_out.println(DateUtil.long2DateString(message.getTime())+"  "+((isOwner)?"我":message.getSenderName())+" :");
    if (message.getStatus() == 0){
      show(message.getMsg()+"（未读）");
    }else {
      show(message.getMsg());
    }
  }

  public void showError(String error) {
    show(error);
  }

}
