package edu.ccrm.util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Backup utility using NIO.2 walkFileTree and recursive directory size calculator.
 */
public class BackupUtil {

    public static Path copyToTimestampedBackup(Path source, Path backupRoot) throws IOException {
        String ts = DateTimeFormatter.ISO_INSTANT.format(Instant.now()).replace(":", "-");
        Path dest = backupRoot.resolve("backup-" + ts);
        Files.createDirectories(dest);
        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path target = dest.resolve(source.relativize(dir));
                Files.createDirectories(target);
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, dest.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
        return dest;
    }

    public static long computeDirectorySize(Path dir) throws IOException {
        final long[] size = {0};
        Files.walkFileTree(dir, new SimpleFileVisitor<>() {
            @Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                size[0] += attrs.size();
                return FileVisitResult.CONTINUE;
            }
        });
        return size[0];
    }
}
