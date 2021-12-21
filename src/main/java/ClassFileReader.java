import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class ClassFileReader {
    private static final String prefix = "./src/main/resources/";
    private String filePath ;

    public ClassFileReader(){
        Properties props = new Properties();
        try {
            FileInputStream fis = new FileInputStream(prefix +"config.properties");
            props.load(fis);
            filePath = prefix + props.getProperty("location");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        ClassFileReader classReader = new ClassFileReader();
        ClassFileStructure.ClassFileInfo cfi = new ClassFileStructure.ClassFileInfo();
        ClassFileStructure.ConstantPool cps = new ClassFileStructure.ConstantPool();
        String filePath = classReader.getFilePath();
        try(FileInputStream fis =  new FileInputStream(filePath)) {
            fis.read(cfi.magicNumbers);
            fis.read(cfi.classVersionSub);
            fis.read(cfi.classVersionMain);
            fis.read(cps.constantPoolCount);
            int constantDataLength = ClassFileStructure.bytes4ToInt(cps.constantPoolCount);
            readConstantData(fis,constantDataLength);
            fis.read(cfi.accessFlags);
            fis.read(cfi.thisClass);
            fis.read(cfi.parentClass);
            fis.read(cfi.interfaceCount);
            int interfaceDataLength = ClassFileStructure.bytes4ToInt(cfi.interfaceCount);
            readInterfaces(fis,interfaceDataLength);
            fis.read(cfi.filedCount);
            int fields = ClassFileStructure.bytes4ToInt(cfi.filedCount);

        } catch (IOException e) {
            e.printStackTrace();
        }
        cfi.printMagicNumbers();
        System.out.println();
        cfi.printClassVersion();
        System.out.println();
        cps.printConstantPoolCount();
        cps.printConstantPoolData();
        System.out.println(cfi.getAccessFlags());
        System.out.println(ClassFileStructure.ClassFileInfo.getClassInfo(cfi.thisClass));
        System.out.println(ClassFileStructure.ClassFileInfo.getClassInfo(cfi.parentClass));
        System.out.println(ClassFileStructure.ClassFileInfo.interfaces);
    }
    public String getFilePath() {
        return filePath;
    }
    public static void readConstantData(FileInputStream fis,int length) throws IOException {
        HashMap<Integer, Integer> constantLenMap = ClassFileStructure.ConstantPool.constantLenMap;
        HashMap<Integer, byte[]> constantDataMap = ClassFileStructure.ConstantPool.constantDataMap;
        for (int i = 1; i < length; i++) {
            byte[] tagData = new byte[1];
            fis.read(tagData);
            int tag = tagData[0];
            if (tag==1){
                byte[] utf8len = new byte[2];
                fis.read(utf8len);
                int len = ClassFileStructure.bytes4ToInt(utf8len);
                byte[] data = new byte[len];
                fis.read(data);
                byte[] allData = new byte[len+3];
                allData[0] = tagData[0];
                System.arraycopy(utf8len,0,allData,1,2);
                System.arraycopy(data,0,allData,3,data.length);
                constantDataMap.put(i,allData);
            }else {
                Integer dataLen = constantLenMap.get(tag);
                byte[] data = new byte[dataLen];
                byte[] allData = new byte[dataLen+1];
                fis.read(data);
                allData[0] = tagData[0];
                System.arraycopy(data, 0, allData, 1, allData.length - 1);
                constantDataMap.put(i,allData);
            }
        }
    }
    public static void readInterfaces(FileInputStream fis,int length) throws IOException {
        List<String> interfaces = ClassFileStructure.ClassFileInfo.interfaces;
        List<Byte> interfacesDataList = ClassFileStructure.ClassFileInfo.interfacesDataList;
        for (int i = 0; i < length; i++) {
            byte[] interfaceData = new byte[2];
            fis.read(interfaceData);
            interfacesDataList.add(interfaceData[0]);
            interfacesDataList.add(interfaceData[1]);
            String interfaceInfo = ClassFileStructure.ClassFileInfo.getClassInfo(interfaceData);
            interfaces.add(interfaceInfo);
        }
    }
    public static void readFieldInfo(FileInputStream fis,int size){

    }
    public static void readMethodInfo(FileInputStream fis,int size){

    }
}
