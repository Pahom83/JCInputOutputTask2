import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static final String gamePath = System.getProperty("user.home") + "/Games/GunRunner";
    public static final String gameSavePath = gamePath + "/savegames";

    public static void main(String[] args) {
        GameProgress gamer1 = new GameProgress(90, 5, 8, 15.5);
        GameProgress gamer2 = new GameProgress(100, 3, 5, 10.0);
        GameProgress gamer3 = new GameProgress(70, 8, 12, 18.2);
        saveGame(gamer1, gameSavePath + "/gamer1.save");
        saveGame(gamer2, gameSavePath + "/gamer2.save");
        saveGame(gamer3, gameSavePath + "/gamer3.save");
        zipFiles(gameSavePath + "/savegames.zip"
                , Arrays.asList(gameSavePath + "/gamer1.save"
                        , gameSavePath + "/gamer2.save"
                        , gameSavePath + "/gamer3.save"));
        deleteNonZip(gameSavePath);

    }

    private static void deleteNonZip(String path) {
        Arrays.stream(new File(path).listFiles())
                .filter(file -> !file.getName().endsWith("zip"))
                .forEach(File::delete);
    }

    private static void saveGame(GameProgress gamer, String s) {
        try (FileOutputStream fos = new FileOutputStream(s)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(gamer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void zipFiles(String nameZip, List<String> filesInZipEntry) {
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(nameZip))) {
            for (String s : filesInZipEntry) {
                File file = new File(s);
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry entry = new ZipEntry(file.getName());
                    out.putNextEntry(entry);
// считываем содержимое файла в массив byte
                    byte[] buffer = new byte[fis.available()];
                    if (fis.read(buffer) != -1){
                        out.write(buffer);
                    }
// добавляем содержимое к архиву
// закрываем текущую запись для новой записи
                    out.closeEntry();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
