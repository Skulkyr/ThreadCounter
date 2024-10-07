package org.pogonin;


public class App 
{
    public static void main( String[] args )
    {
        var counter = new MultiThreadCounter();
        counter.startCounting(5, 0, 100);
//        var counter = new TwoThreadCounter();
//        counter.startCounting(0, 100);
    }
}
