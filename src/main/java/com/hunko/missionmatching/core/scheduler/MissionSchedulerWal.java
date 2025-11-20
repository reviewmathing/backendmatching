package com.hunko.missionmatching.core.scheduler;

import com.hunko.missionmatching.core.domain.Mission;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MissionSchedulerWal {

    private final FileChannel logFile;
    private final Path logPath;

    public MissionSchedulerWal(String logPath, String failName) {
        String fileName = getFileName(logPath, failName);
        this.logPath = Paths.get(fileName);
        try {
            this.logFile = FileChannel.open(
                    this.logPath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addLog(WalMissionDto walMissionDto) {
        try {
            String line = String.join(",", walMissionDto.missionId().toString(), walMissionDto.time().toString(), walMissionDto.queueActionType().toString());
            ByteBuffer buffer = ByteBuffer.wrap((line + "\n").getBytes());
            logFile.write(buffer);
            logFile.force(true);  // 중요: crash-safe
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<WalMissionDto> readAll() {
        try {
            return toDto( Files.readAllLines(this.logPath));
        } catch (IOException e) {
            throw new RuntimeException("WAL read failed", e);
        }
    }

    private List<WalMissionDto> toDto(List<String> strings) {
        List<WalMissionDto> walMissionDtos = new ArrayList<>();
        for (String string : strings) {
            String[] split = string.split(",");
            walMissionDtos.add(
                    new WalMissionDto(
                            Long.valueOf(split[0]),
                            LocalDateTime.parse(split[1]),
                            QueueActionType.valueOf(split[2])
                    )
            );
        }
        return walMissionDtos;
    }

    private static String getFileName(String logPath, String failName) {
        if (logPath.endsWith("/")) {
            return logPath + failName + ".log";
        }
        return logPath + "/" + failName + ".log";
    }


}
