import java.util.HashMap;

public class ClassFileStructure {

    public static class ClassFileInfo {
        public byte[] magicNumbers = new byte[4];
        public byte[] classVersionMain = new byte[2];
        public byte[] classVersionSub = new byte[2];
        public void  printMagicNumbers(){
            int i = bytes4ToInt(magicNumbers);
            System.out.printf("%x",i);
        }
        public void printClassVersion(){
            int main = bytes4ToInt(classVersionMain);
            int sub = bytes4ToInt(classVersionSub);
            System.out.printf("main:%d,sub:%d",main,sub);
        }
    }
    public static class ConstantPool{
        public static HashMap<Integer, Integer> constantLenMap = new HashMap<>();
        public static HashMap<Integer,byte[]> constantDataMap = new HashMap<>();
        public static HashMap<Integer,String> constantTypeMap = new HashMap<>();
        static {
            constantLenMap.put(1, 2);
            constantLenMap.put(3, 4);
            constantLenMap.put(4, 4);
            constantLenMap.put(5, 8);
            constantLenMap.put(6, 8);
            constantLenMap.put(7, 2);
            constantLenMap.put(8, 2);
            constantLenMap.put(9, 4);
            constantLenMap.put(10, 4);
            constantLenMap.put(11, 4);
            constantLenMap.put(12, 4);
            constantLenMap.put(15, 3);
            constantLenMap.put(16, 2);
            constantLenMap.put(17, 4);
            constantLenMap.put(18, 4);
            constantLenMap.put(19, 2);
            constantLenMap.put(20, 2);

            constantTypeMap.put(1,"CONSTANT_Utf8");
            constantTypeMap.put(3,"CONSTANT_Integer");
            constantTypeMap.put(4,"CONSTANT_Float");
            constantTypeMap.put(5,"CONSTANT_Long");
            constantTypeMap.put(6,"CONSTANT_Double");
            constantTypeMap.put(7,"CONSTANT_Class");
            constantTypeMap.put(8,"CONSTANT_String");
            constantTypeMap.put(9,"CONSTANT_Fieldref");
            constantTypeMap.put(10,"CONSTANT_Methodref");
            constantTypeMap.put(11,"CONSTANT_InterfaceMethodref");
            constantTypeMap.put(12,"CONSTANT_NameAndType");
            constantTypeMap.put(15,"CONSTANT_MethodHandle");
            constantTypeMap.put(16,"CONSTANT_MethodType");
            constantTypeMap.put(17,"CONSTANT_Dynamic");
            constantTypeMap.put(18,"CONSTANT_InvokeDynamic");
            constantTypeMap.put(19,"CONSTANT_Module");
            constantTypeMap.put(20,"CONSTANT_Package");


        }
        public byte[] constantPoolCount = new byte[2];
        public void printConstantPoolCount(){
            System.out.println(bytes4ToInt(constantPoolCount));
        }
        public void printConstantPoolData(){
            constantDataMap.forEach(
                    (key,value)->{
                        System.out.print("#"+key+"\t");
                        int tag = value[0];
                        System.out.print(constantTypeMap.get(tag)+"\t");
                        if (tag==1){
                            String s = new String(value, 3, value.length-3);
                            System.out.print(s);
                        }
                        System.out.println();
                    }
            );
        }
    }
    public static int bytes4ToInt(byte[] data){
        int value = 0;
        for (int i = 0; i < data.length ; i++) {
            int shift = (data.length-1-i)*8;
            value += ((data[i] & 0xFF) << shift);
        }
        return value;
    }
}
