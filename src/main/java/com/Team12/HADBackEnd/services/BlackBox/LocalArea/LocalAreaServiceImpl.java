package com.Team12.HADBackEnd.services.BlackBox.LocalArea;

import com.Team12.HADBackEnd.models.District;
import com.Team12.HADBackEnd.models.LocalArea;
import com.Team12.HADBackEnd.DTOs.LocalArea.CreateLocalAreasRequest;
import com.Team12.HADBackEnd.repository.DistrictRepository;
import com.Team12.HADBackEnd.repository.LocalAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocalAreaServiceImpl implements LocalAreaService {

    private final LocalAreaRepository localAreaRepository;

    private final DistrictRepository districtRepository;

    @Autowired
    public LocalAreaServiceImpl(LocalAreaRepository localAreaRepository,
                                DistrictRepository districtRepository) {
        this.localAreaRepository = localAreaRepository;
        this.districtRepository = districtRepository;
    }
    @Override
    public String createLocalAreasInDistrict(CreateLocalAreasRequest request) {
        Long districtId = request.getDistrictId();
        List<LocalArea> localAreas = request.getLocalAreas();

        District district = districtRepository.findById(districtId)
                .orElseThrow(() -> new RuntimeException("District not found with id: " + districtId));

        for (LocalArea localArea : localAreas) {
            localArea.setDistrict(district);
            district.getLocalAreas().add(localArea);
            localAreaRepository.save(localArea);
        }

        districtRepository.save(district);

        return "Local areas created successfully in district with id: " + districtId;
    }
}
