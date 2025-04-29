package vn.iotstar.DatingApp.Service;

import org.springframework.stereotype.Service;

import vn.iotstar.DatingApp.Entity.Reports;

@Service
public interface IReportService {

	<S extends Reports> S save(S entity);

}
