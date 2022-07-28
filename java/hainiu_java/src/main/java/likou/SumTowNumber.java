package likou;

import java.util.*;
import java.util.Map;

// 1.两数之和

/**
 * 给定一个整数数组 nums 和一个整数目标值 target，
 * 请你在该数组中找出 和为目标值 target 的那 两个整数，并返回它们的数组下标。
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
 * 你可以按任意顺序返回答案。
 */
@SuppressWarnings("ALL")
public class SumTowNumber {

    // 使用双重 for 循环 一个一个进行比较
    public static HashMap<Integer, Integer> findNum(int[] arr, int sum) {
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        int i;
        int j;
        for (i = 0; i < arr.length - 2; i++) {
            for (j = i + 1; j < arr.length; j++) {
                if (i == j) {
                    continue;
                }
                if (arr[i] + arr[j] == sum) {
//                    hashMap.put(arr[i], i);
//                    hashMap.put(arr[j], j);
                    hashMap.put(i, j);
                }
            }
        }
        return hashMap;
    }

    // 创建 map 保存遍历到的值相差的数据 和 当前遍历值的索引下标
    public static int[] twoSum(int[] nums, int target) {
        int[] indexs = new int[2];
        // 创建 map 保存遍历到的值相差的数据 和 当前遍历值的索引下标
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            // 查询 map 中是否存在差值
            if (hashMap.containsKey(nums[i])) {
                // 如果存在,将原值的索引下标插入
                indexs[0] = hashMap.get(nums[i]);
                // 插入 差值的索引下标
                indexs[1] = i;
            }
            hashMap.put(target - nums[i], i);
        }
        return indexs;
    }

    public static void main(String[] args) {
        int[] arr = {5, 3, 1, 2, 4};
        int sum = 7;
        HashMap<Integer, Integer> resultMap = findNum(arr, sum);
        Iterator<Map.Entry<Integer, Integer>> iterator = resultMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            System.out.println("key：" + entry.getKey() + "\t" + "value：" + entry.getValue());
        }

        int[] ints = twoSum(arr, sum);
        for (int i : ints) {
            System.out.println(i);
        }

    }
}
