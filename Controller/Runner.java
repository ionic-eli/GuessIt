package Controller;
import View.MainScreen;

public class Runner 
{
    public static void main(String[] args) 
    {
        new MainScreen(ReadWriteData.readData());
    }
}