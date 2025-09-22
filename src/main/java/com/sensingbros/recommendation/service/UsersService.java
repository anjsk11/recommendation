package com.sensingbros.recommendation.service;

import com.sensingbros.recommendation.domain.Gps;
import com.sensingbros.recommendation.model.UsersDTO;
import com.sensingbros.recommendation.domain.Users;
import com.sensingbros.recommendation.mapper.UsersMapper;
import com.sensingbros.recommendation.repository.UsersRepository;
import com.sensingbros.recommendation.repository.GpsRepository;
import com.sensingbros.recommendation.util.HeatmapUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalTime;

import java.util.Optional;
import java.util.UUID;

@Service
public class UsersService {

    private final UsersMapper usersMapper;
    private final UsersRepository usersRepository;
    private final GpsRepository gpsRepository;

    @Autowired
    public UsersService(UsersMapper usersMapper, UsersRepository usersRepository, GpsRepository gpsRepository) {
        this.usersMapper = usersMapper;
        this.usersRepository = usersRepository;
        this.gpsRepository = gpsRepository;
    }

    // UserDto를 User 엔티티로 변환하여 저장 (예시로 DB에 저장)
    public void syncUser(UUID id, String name) {
        if (!usersRepository.existsById(id)) {
            // UserDto → User 엔티티로 변환
            Users user = new Users();
            user.setId(id); // userDto 안의 id 가져오기
            user.setName(name); // userDto 안의 name 가져오기
            usersRepository.save(user);  // DB에 저장
        }
    }

    // DB에서 데이터를 받아와 UsersDTO로 변환
    public Optional<UsersDTO> getUserById(UUID id) {
        return usersRepository.findById(id)
                .map(usersMapper::toDto);
    }

    public void deleteUserById(UUID id) {
        usersRepository.deleteById(id);
    }

    @Transactional
    public void updateUserHeatmap(UUID userId, double lat, double lng) {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalTime now = LocalTime.now();
        int hour = now.getHour();

        int[] index = HeatmapUtils.getGridIndex(lat, lng);
        int row = index[0];
        int col = index[1];

        Integer[][] heatmap;
        if (hour >= 0 && hour < 8) { // 오전
            heatmap = user.getMorningHeatmap();
            heatmap[row][col] = heatmap[row][col] + 1;
            user.setMorningHeatmap(heatmap);
        } else if (hour >= 8 && hour < 16) { // 오후
            heatmap = user.getNoonHeatmap();
            heatmap[row][col] = heatmap[row][col] + 1;
            user.setNoonHeatmap(heatmap);
        } else { // 밤
            heatmap = user.getNightHeatmap();
            heatmap[row][col] = heatmap[row][col] + 1;
            user.setNightHeatmap(heatmap);
        }

        Gps gps = new Gps();
        gps.setUser(user);
        gps.setLongitude(lng);
        gps.setLatitude(lat);

        usersRepository.save(user);
        gpsRepository.save(gps);
    }

    public Integer[][] getCombinedHeatmap(UUID userId) {
        // 사용자 엔티티 가져오기
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Integer[][] morning = user.getMorningHeatmap();
        Integer[][] noon = user.getNoonHeatmap();
        Integer[][] night = user.getNightHeatmap();

        // heatmap null 체크
        if (morning == null || noon == null || night == null) {
            throw new RuntimeException("One of the heatmaps is null");
        }

        int rows = morning.length;
        int cols = morning[0].length;

        Integer[][] combined = new Integer[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                combined[i][j] =
                        (morning[i][j] != null ? morning[i][j] : 0) +
                                (noon[i][j] != null ? noon[i][j] : 0) +
                                (night[i][j] != null ? night[i][j] : 0);
            }
        }

        return combined;
    }

    private void addHeatmap(Integer[][] combined, Integer[][] source) {
        if (source == null) return;
        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source[i].length; j++) {
                combined[i][j] += source[i][j] != null ? source[i][j] : 0;
            }
        }
    }
}

