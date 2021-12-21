import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassFileStructure {

    public static class ClassFileInfo {
        public static HashMap<Integer, String> accessKeyMap = new HashMap<>();
        static {
            accessKeyMap.put(0x0001&0xFFFF,"PUBLIC");
            accessKeyMap.put(0x0010&0xFFFF,"FINAL");
            accessKeyMap.put(0x0020&0xFFFF,"SUPER");
            accessKeyMap.put(0x0200&0xFFFF,"INTERFACE");
            accessKeyMap.put(0x0400&0xFFFF,"ABSTRACT");
            accessKeyMap.put(0x1000&0xFFFF,"SYNTHETIC");
            accessKeyMap.put(0x2000&0xFFFF,"ANNOTATION");
            accessKeyMap.put(0x4000&0xFFFF,"ENUM");
            accessKeyMap.put(0x8000&0xFFFF,"MODULE");
        }
        public static List<String> interfaces = new ArrayList<>();
        public byte[] magicNumbers = new byte[4];
        public byte[] classVersionSub = new byte[2];
        public byte[] classVersionMain = new byte[2];
        public byte[] accessFlags = new byte[2];
        public byte[] thisClass = new byte[2];
        public byte[] parentClass = new byte[2];
        public byte[] interfaceCount = new byte[2];
        public static List<Byte> interfacesDataList = new ArrayList<>();
        public byte[] filedCount = new byte[2];
        public static List<FieldMethodInfo> fieldInfos = new ArrayList<>();
        public byte[] methodCount = new byte[2];
        public static List<FieldMethodInfo> methodInfos = new ArrayList<>();
        public void  printMagicNumbers(){
            int i = bytes4ToInt(magicNumbers);
            System.out.printf("%x",i);
        }
        public void printClassVersion(){
            int main = bytes4ToInt(classVersionMain);
            int sub = bytes4ToInt(classVersionSub);
            System.out.printf("main:%d,sub:%d",main,sub);
        }
        public List<String> getAccessFlags(){
            List<String> flags = new ArrayList<>();
            int flagsData = bytes4ToInt(accessFlags);
            accessKeyMap.forEach((k,v)->{
                if ((flagsData&k)==1){
                    flags.add(v);
                }
            });
            return flags;
        }
        public static String  getClassInfo(byte[] infoData){
            int index1  = bytes4ToInt(infoData);
            byte[] classData = ConstantPool.constantDataMap.get(index1);
            byte[] indexData = new byte[2];
            System.arraycopy(classData,1,indexData,0,2);
            int index2 = bytes4ToInt(indexData);
            byte[] realData = ConstantPool.constantDataMap.get(index2);
            return new String(realData, 3, realData.length-3);
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
            System.out.println(bytes4ToInt(constantPoolCount)-1);
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
    public static class FieldMethodInfo{
        public static HashMap<Integer, String> fieldAccessKeyMap = new HashMap<>();
        public static HashMap<Integer, String> methodAccessKeyMap = new HashMap<>();
        static {
            fieldAccessKeyMap.put(0x0001&0xFFFF,"PUBLIC");
            fieldAccessKeyMap.put(0x0002&0xFFFF,"PRIVATE");
            fieldAccessKeyMap.put(0x0004&0xFFFF,"PROTECTED");
            fieldAccessKeyMap.put(0x0008&0xFFFF,"STATIC");
            fieldAccessKeyMap.put(0x0010&0xFFFF,"FINAL");
            fieldAccessKeyMap.put(0x0040&0xFFFF,"VOLATILE");
            fieldAccessKeyMap.put(0x0080&0xFFFF,"TRANSIENT");
            fieldAccessKeyMap.put(0x1000&0xFFFF,"SYNTHETIC");
            fieldAccessKeyMap.put(0x4000&0xFFFF,"ENUM");

            methodAccessKeyMap.put(0x0001&0xFFFF,"PUBLIC");
            methodAccessKeyMap.put(0x0002&0xFFFF,"PRIVATE");
            methodAccessKeyMap.put(0x0004&0xFFFF,"PROTECTED");
            methodAccessKeyMap.put(0x0008&0xFFFF,"STATIC");
            methodAccessKeyMap.put(0x0010&0xFFFF,"FINAL");
            methodAccessKeyMap.put(0x0020&0xFFFF,"SYNCHRONIZED");
            methodAccessKeyMap.put(0x0040&0xFFFF,"BRIDGE");
            methodAccessKeyMap.put(0x0080&0xFFFF,"VARARGS");
            methodAccessKeyMap.put(0x0100&0xFFFF,"NATIVE");
            methodAccessKeyMap.put(0x0400&0xFFFF,"ABSTRACT");
            methodAccessKeyMap.put(0x0800&0xFFFF,"STRICT");
            methodAccessKeyMap.put(0x1000&0xFFFF,"SYNTHETIC");
        }
        byte[] accessFlags = new byte[2];
        byte[] nameIndex = new byte[2];
        byte[] descriptorIndex = new byte[2];
        byte[] attrCount = new byte[2];
        List<Byte> attributesInfo = new ArrayList<>();
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
