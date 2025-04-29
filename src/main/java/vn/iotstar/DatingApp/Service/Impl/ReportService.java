package vn.iotstar.DatingApp.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.Reports;
import vn.iotstar.DatingApp.Repository.ReportsRepository;
import vn.iotstar.DatingApp.Service.IReportService;

@Service
public class ReportService implements IReportService {
	@Autowired
	ReportsRepository reportRepository;

	@Override
	public <S extends Reports> S save(S entity) {
		return reportRepository.save(entity);
	}
	
	

}
