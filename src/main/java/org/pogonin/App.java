package org.pogonin;


public class App 
{
    public static void main( String[] args )
    {
        var counter = new MultiThreadCounter();
        counter.startCounting(2, 0, 8);
//        var counter = new TwoThreadCounter();
//        counter.startCounting(0, 100);
    }
}
