package com.example.school.service.revenue;

import com.example.school.entity.Classroom;
import com.example.school.entity.Revenue;
import com.example.school.entity.RevenueClass;
import com.example.school.repository.RevenueClassRepository;
import com.example.school.service.ClassroomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RevenueClassServiceImp implements RevenueClassService {
    private RevenueClassRepository revenueClassRepository;
    private ClassroomService classroomService;
    private RevenueService revenueService;

    @Autowired
    public RevenueClassServiceImp(RevenueClassRepository revenueClassRepository, ClassroomService classroomService,
                                 RevenueService revenueService ) {
        this.revenueClassRepository = revenueClassRepository;
        this.classroomService = classroomService;
        this.revenueService = revenueService;
    }

    @Override
    public List<RevenueClass> getRevenueClassByRevenue(int id) {
        List<RevenueClass> revenueClasses = revenueClassRepository.getRevenueClassByRevenue_Id(id);
        return revenueClasses;
    }

    @Override
    public List<RevenueClass> getRevenueClassByClass(int id) {
        return revenueClassRepository.getRevenueClassByClassroom_Id(id);
    }

    @Override
    public boolean deleteRevenueClass(int id, String idClass) {
        String[] idString = idClass.split(",");
        for (int i = 0; i <idString.length; i++) {
            RevenueClass revenueClass = revenueClassRepository.
                    findByRevenue_IdAndClassroom_Id(id,Integer.parseInt(idString[i]));
            revenueClassRepository.delete(revenueClass);
        }
        return true;
    }

    @Override
    public boolean addRevenueClass(int id, String idClass) {
        String[] idString = idClass.split(",");
        for (int i = 0; i <idString.length; i++) {

            Revenue revenue = revenueService.getRevenueById(id);
            Classroom classroom = classroomService.findById(Integer.parseInt(idString[i]));
            RevenueClass revenueClass = new RevenueClass();
            revenueClass.setRevenue(revenue);
            revenueClass.setClassroom(classroom);
            revenueClassRepository.save(revenueClass);
        }
        return true;
    }
}
