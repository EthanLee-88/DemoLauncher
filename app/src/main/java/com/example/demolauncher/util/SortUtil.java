package com.example.demolauncher.util;

import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SortUtil {
    private static List<Integer> list = new ArrayList<>();
    private static final Integer[] sortArray = new Integer[]{12 , 3 , 1 , 18 , 22 ,
            129 , 6 , 9 , 13 , 32 };
    private static final int[] intArray = new int[]{12 , 3 , 1 , 18 , 22 ,
            129 , 6 , 9 , 13 , 32 };
    static {
        list.add(12);list.add(3);list.add(1);list.add(18);list.add(22);
        list.add(129);list.add(6);list.add(9);list.add(13);list.add(32);
    }

    public static void sort(){
        bubbleSort(list);
        bubbleSort(sortArray);
        quick(intArray);
    }

    public static List<Integer> bubbleSort(List<Integer> sort){
//        int temp;
//        for (int i = 0 ; i < sort.size() ; i ++){
//            for (int j = 0 ; j < sort.size() - i - 1 ; j ++){
//                if (sort.get(j) > sort.get(j + 1)){
//                    temp = sort.get(j);
//                    sort.add(j , sort.get(j + 1));
//                    sort.add(j + 1 , temp);
//                }
//            }
//        }
//        Log.d("integerSort=" , "sort=" + sort.toString());
        return sort;
    }

    /**
     * 冒泡
     *
     * @param sort
     * @return
     */
    public static Integer[] bubbleSort(Integer[] sort){
        int temp;
        for (int i = 0 ; i < sort.length ; i ++){
            for (int j = 0 ; j < sort.length - i -1 ; j ++ ){
                if (sort[j] > sort[j + 1]){
                    temp = sort[j];
                    sort[j] = sort[j + 1];
                    sort[j + 1] = temp;
                }
            }
        }
        Log.d("integerSort=" , "array=" + Arrays.toString(sort));
        return sort;
    }

    public static void quick(int[] sort){
        if (sort.length > 0) quickReal(sort , 0 , sort.length - 1);
        Log.d("integerSort=" , "Qui=" + Arrays.toString(sort));
    }
    private static void quickReal(int[] sortR , int low , int high){
        if (low < high){
            int middleIndex = quickMiddleSort(sortR , low , high); // 划分数组片段，并记录片段中值索引
            quickReal(sortR , low , middleIndex - 1); // 数组片段的下半部分进行递归
            quickReal(sortR , middleIndex + 1 , high); //数组片段的上半部分进行递归
        }
    }
    /************  将一段数组以中值为分界
     *                大的值移到中值右边
     *                小的移到左边
     *                该方法只是按中值大小将数组片段分成两半，
     *                并未实现彻底排序，还需进一步递归***************/
    private static int quickMiddleSort(int[] sortA , int low , int high){
        int middleTemp = sortA[low];  // 保存中值
        while (low < high){    //low和high相撞前一直循环
            while ((low < high) && (sortA[high] >= middleTemp)) {
                high --;  // 从高处往下遍历，遇到比中值小的值停止循环
            }
            sortA[low] = sortA[high];  //将比中值小的值放到低处
            while ((low < high) && (sortA[low] <= middleTemp)){
                low ++;   // 从低处向上遍历，碰到比中值大的值停止循环
            }
            sortA[high] = sortA[low]; //将比中值大的值移动到右边
        }
        sortA[low] = middleTemp;  // 还原中值
        return low;
    }

    public static void heapSort(){
        heapSort(intArray);
    }

    private static void heapSort(int[] sort){
        if (sort == null || sort.length < 2) return;
        for (int i = sort.length / 2 - 1 ; i >= 0 ; i --){
            percDown(sort , i , sort.length);
        }
        for (int i = sort.length - 1 ; i > 0 ; i --){
            swapValue(sort , 0 , i);
            percDown(sort , 0 , i);
        }

        Log.d("tag" , "heapSort=" + Arrays.toString(sort));
    }

    /**
     * 下虑
     *
     * @param sort
     * @param index
     * @param length
     */
    private static void percDown(int[] sort , int index , int length){
        Looper.loop();
        while (index * 2 + 1 < length){
            if ((index * 2 + 2 < length) && (sort[index * 2 + 1] < sort[index * 2 + 2])){
                if (sort[index] < sort[2 * 2 + 2])
                {
                    swapValue(sort , index , index * 2 + 2);
                    index = index * 2 + 2;
                }else break;
            }else if (sort[index] < sort[index * 2 + 1]){
                swapValue(sort , index , index * 2 + 1);
                index = index * 2 + 1;
            }else break;
        }
    }

    /**
     * 交换
     *
     * @param sort
     * @param one
     * @param two
     */
    private static void swapValue(int[] sort , int one , int two){
        int team = sort[one];
        sort[one] = sort[two];
        sort[two] = team;
    }

}
