package likou;


/**
 * @Auther: wxf
 * @Date: 2022/5/23 15:05:01
 * @Description: Solution
 * @Version 1.0.0
 */
public class Solution {

    public static void main(String[] args) {
        int length = lengthOfLongestSubstring("dvdf");
        System.out.println(length);
    }

    public static int lengthOfLongestSubstring(String s){
        // 记录字符上一次出现的位置
        int[] last = new int[128];
        for(int i = 0; i < 128; i++) {
            last[i] = -1;
        }
        int n = s.length();

        int res = 0;
        int start = 0; // 窗口开始位置
        for(int i = 0; i < n; i++) {
            // 返回 索引位置的 char 值
            int index = s.charAt(i);
            start = Math.max(start, last[index] + 1);
            res   = Math.max(res, i - start + 1);
            last[index] = i;
        }

        return res;
    }

//    public static int lengthOfLongestSubstring(String s) {
//        StringBuilder stringBuilder = new StringBuilder();
//        int index = 0;
//        int length = 0;
//        if(0 == s.length()){
//            return 0;
//        }
//        String[] split = s.split("");
//        String[] str = new String[split.length];
//        String[] maxString = new String[1];
//
//        for (String s1 : split) {
//            if (!(stringBuilder.toString().contains(s1))) {
//                if(null == str[index]){
//                    str[index] = s1;
//                    stringBuilder.append(s1);
//                    continue;
//                }
//                str[index] = str[index] + s1;
//                stringBuilder.append(s1);
//            }else{
//                index += 1;
//                if(null == str[index]){
//                    str[index] = s1;
//                    stringBuilder.append(s1);
//                    continue;
//                }
//                str[index] = str[index] + s1;
//            }
//        }
//        for (int i = 0; i < index + 1; i++) {
//            if(str[i].length() > length){
//                maxString[0] = str[i];
//                length = str[i].length();
//            }
//        }
//        System.out.println(maxString[0]);
//        return length;
//    }

}