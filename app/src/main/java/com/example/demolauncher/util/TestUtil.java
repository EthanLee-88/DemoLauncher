package com.example.demolauncher.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.TreeSet;

public class TestUtil {
    private static boolean isFix = false;
    public static void showToast(Context context){
        if (context != null) {
            if (!isFix){
                Toast.makeText(context , "碧云天" , Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context , "黄花地" , Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 字符串转换成数组。从左往右，每次加下个数前乘10.
    // 要考虑正负数以及溢出的问题，鲁棒性
    public static int turnString(String abc) throws NumberFormatException{
        if (!isDigital(abc)) throw new NumberFormatException("e");
        int intValue = 0;
        boolean negative = false;
        for (int i = 0 ; i < abc.length() ; i ++){
            if (i == 0){
                if (abc.charAt(i) == '-'){
                    negative = true;
                    continue;
                }else if (abc.charAt(i) == '+'){
                    negative = false;
                    continue;
                }else {
                    negative = false;
                }
            }
            Log.d("tag" , "intValueBefore=" + intValue);
            if (intValue > (Integer.MAX_VALUE / 10)) throw new NumberFormatException("e");
            intValue *= 10;
            if (intValue > Integer.MAX_VALUE - (abc.charAt(i) - '0')) throw new NumberFormatException("e");
            intValue += (abc.charAt(i) - '0');
        }
        if (negative) intValue = -intValue;
        Log.d("tag" , "intValue=" + intValue);
        return intValue;
    }
    //判断字符串是否符合数字格式，鲁棒
    public static boolean isDigital(String str){
        if ((str == null) || (str.length() == 0)) return false;
        if (!((str.charAt(0) > '0') && (str.charAt(0) < '9'))){
            if (str.length() == 1){
                return false;
            }else {
                if (!((str.charAt(0) == '-') || (str.charAt(0) == '+'))) return false;
            }
        }
        for (int i = 1 ; i < str.length() ; i ++){
            if (!((str.charAt(i) >= '0') && (str.charAt(i) <= '9'))){
                return false;
            }
        }
        return true;
    }

    // f(1) = 1 , f(2) = 2 , f(n) = f(n-1) + f(n-2);
    // 已知方向向位置方向递归
    public static int fibonacci(int n){
        if (n < 1) throw new NumberFormatException("e");
        if ((n == 1) || (n == 2)) return n;

        int fn = 0;
        int low = 1;
        int high = 2;
        for (int i = 3 ; i <= n ; i ++ ){
            fn = low + high;
            low = high;
            high = fn;
        }
        Log.d("tag" , "fn=" + fn + "\nRun=" + fibonacciRun(n));
        return fn;
    }
    // 斐波那契数列，未知方向向已知方向递归
    public static int fibonacciRun(int n){
        if (n < 1) throw new NumberFormatException("e");
        if ((n == 1) || (n == 2)) return n;
        return (fibonacciRun(n - 1) + fibonacciRun(n - 2));
    }
    // n , 0 ~ n-1  找数组里重复数字
    // 也可以增加空间复杂度用哈希表实现
    public static boolean duplicate(int[] de){
        int found = -1;
        if ((de == null) || (de.length <= 1)) throw new NumberFormatException("e");
        for (int i = 1 ; i < de.length - 1 ; i ++){
            if ((de[i] < 0) || (de[i]) >= de.length) throw new NumberFormatException("e");
        }
        for (int j = 0 ; j < de.length ; j ++){
            while(j != de[j]){
                if (de[j] == de[de[j]]){
                    found = de[j];
                    return true;
                }
                int tem = de[j];
                de[j] = de[de[j]];
                de[de[j]] = tem;
            }
        }
        return false;
    }
    // 二维数组二分查找
    public static void findLocation(int[][] square , int rows , int columns , int n){
        if (square == null) throw new NumberFormatException("e");
        if (square.length < 1) throw new NumberFormatException("");
        boolean found = false;
        int row = 0;
        int column = columns - 1;
        while ((row < rows) || (column > 0)){
            if (square[row][column] == n){
                found = true;
                break;
            }else if (square[row][column] > n){
                column --;
            }else {
                row ++;
            }
        }
    }

    //发现二进制数里1出现的个数，用1右移按位与
    public static int findCount(int n){
        int count = 0;
        int flag = 1;
        while (flag != 0){
            if ((n & flag) != 0) count ++;
            flag = flag << 1;
        }
        Log.d("tag" , "count=" + count);
        findCountRun(n);
        return count;
    }
    // 发现二进制数的1的个数。n & (n - 1)能消灭最优先的一个1
    public static int findCountRun(int n){
        int count = 0;
        while (n != 0){
            count ++ ;
            n = n & (n - 1);
        }
        Log.d("tag" , "countRun=" + count);
        return count;
    }
    // 两个数二进制里有多少位时不同的，
    //采用异或，得到结果再计算1的个数
    public static int findDeference(int n , int m){
        int de = m ^ n;
        int count = findCountRun(de);
        return count;
    }
    // 求base的 exponent次方根
    // 需考虑边界条件，鲁棒
    public static double powerExponent(double base , int exponent){
        if (base == 0) throw new NumberFormatException("e");
        if (exponent == 0) return 1;
        int absExponent = exponent < 0 ? -exponent : exponent;
        double result = powerExponentCore(base , absExponent);
        return exponent < 0 ? 1 / result : result;
    }
    private static double powerExponentCore(double base , int unSignExponent){
        double result = 1;
        for (int i = 1 ; i <= unSignExponent ; i ++ ){
            result *= base;
        }
        return result;
    }
    //使用斐波那契公式递归求解一个数的整数次方 , 递归的方法
    private static double powerExponentCoreByFibonacci(double base , int unSignExponent){
        double result = 1;
        if (unSignExponent == 0) return 1;
        if (unSignExponent == 1) return base;
        result = powerExponentCoreByFibonacci(base , unSignExponent >> 1);
        result *= result;
        return (unSignExponent % 2 == 0) ? result : result * base;
    }

    // 排序几万名员工的年龄，
    //这里作0-99岁限制，使用辅助的数组统计
    public static int[] sortIntArray(int[] age){
        if (age == null) throw new NullPointerException("Null");
        if (age.length < 1) throw new NullPointerException("Null");
        int [] ageCount = new int[100];
        for (int i = 0 ; i < age.length ; i ++ ) age[i] = 0;
        for (int j = 0 ; j < age.length ; j ++){
            if ((age[j] < 0) || (age[j] > 99)) new Exception("age out of range !");
            ageCount[age[j]] ++;
        }
        int ageIndex = 0;
        for (int a = 0 ; a < 100 ; a ++){
            for (int k = 0 ; k < ageCount[a] ; k ++){  // 如果岁数次数位置上是0，则说明没有该岁数的人
                age[ageIndex] = a;
                ageIndex ++ ;
            }
        }
        return age;
    }
    //在旋转数组中找到最小数
    public static int findMinNumInArray(int[] numbers){
        if (numbers == null || numbers.length < 1) throw new NullPointerException("null or zero!");
        int indexLow = 0;
        int indexHigh = numbers.length - 1;
        int indexMiddle = indexLow;
        while(numbers[indexLow] >= numbers[indexHigh]){
            if (indexHigh - indexLow == 1){  //找到了交界处
                indexMiddle = indexHigh;
                break;
            }
            indexMiddle = (indexHigh + indexLow) / 2;
            if ((numbers[indexMiddle] == numbers[indexLow]) &&
                    (numbers[indexMiddle] == numbers[indexHigh])) {

            }
            if (numbers[indexMiddle] > numbers[indexLow]) indexLow = indexMiddle;
            else if (numbers[indexMiddle] < numbers[indexHigh]) indexHigh = indexMiddle;
        }
        return numbers[indexMiddle];
    }
    // 按顺序查找
    private static int sortByOrder(int[] numbers , int indexL , int indexH){
        int numberOne = numbers[indexL];
        for (int i = indexL + 1 ; i <= indexH ; i ++ ){
            if (numberOne > numbers[i]) return numbers[i];
        }
        return numberOne;
    }

    // 打印0 - n位最大数，要考虑大数
    public static void printNumberList(int n){
        if (n < 1) throw new NumberFormatException("illegal number length");
        int[] numberStr = new int[n];
        for (int i = 0 ; i < n ; i ++) numberStr[i] = 0;
        StringBuffer printStr = new StringBuffer();
//        printStr.delete(0 , printStr.length() - 1);
        while (!addNumber(numberStr)){
//            for (int j = 0 ; j < numberStr.length - 1 ; j ++){
////                if ((numberStr[j] == 0) && (printStr.length() == 0)) continue;
//                printStr.append(numberStr[j] + "");
//            }
//            Log.d("tag" , "printStr=" + printStr.toString());
//            printStr.delete(0 , printStr.length() - 1);
            Log.d("tag" , "printStr=" + Arrays.toString(numberStr));
        }
    }

    private static boolean addNumber(int[] numbers){
        boolean isTakeOver = false;
        int carryBit = 0; // 进位
        for (int i = numbers.length - 1 ; i >= 0  ; i --){
            int sum = numbers[i] + carryBit; // 最低位加1
            if (i == numbers.length - 1) sum ++;
            if (sum >= 10){  // 需进位
                if (i == 0) isTakeOver = true; //已经到了最大数
                else {
                    sum = 0;
                    carryBit = 1;
                    numbers[i] = sum;
                }
            }else {
                numbers[i] = sum;
                break;
            }
        }
      return isTakeOver;
    }
    // 顺时针打印一个矩阵，也就是二维数组
    public static void printSquare(int[][] square , int rows ,int columns){
        if (square == null || (rows <= 0 || columns <= 0)) return;

        int start = 0; // 起始坐标点（start ， start）
        while ((rows > 2 * start) && (columns > 2 * start)){  // 停止循环的条件
            printCircle(square , rows , columns , start);
            start ++ ;
        }
    }
    // 打印一圈
    private static void printCircle(int[][] numbers , int rows , int columns , int start){
        int rowEnd = rows - 1 - start;
        int columnEnd = columns - 1 - start;
        for (int i = start ; i <= rowEnd ; i ++ ){  // 打印第一行
            int number = numbers[start][i];
            Log.d("tag" , "number=" + number);
        }
        if (start < columnEnd)
        for (int i = start + 1 ; i <= columnEnd ; i ++){
            int number = numbers[i][columnEnd];
            Log.d("tag" , "number=" + number);
        }
        if (rowEnd  > start && columnEnd > start)
        for (int i = rowEnd - 1 ; i >= start ; i --){
            int number = numbers[rowEnd][i];
            Log.d("tag" , "number=" + number);
        }
        if (rowEnd  > start && columnEnd - 1 > start)
        for (int i = columnEnd - 1 ; i > start ; i --){
            int number = numbers[i][start];
            Log.d("tag" , "number=" + number);
        }
    }

    public static void linkedListRun(){
        LinkedList<String> linkedList = new LinkedList();

        TreeMap treeMap = new TreeMap();
        TreeSet treeSet = new TreeSet();
    }
}
