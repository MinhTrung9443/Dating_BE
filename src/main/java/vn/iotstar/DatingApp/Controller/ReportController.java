package vn.iotstar.DatingApp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.iotstar.DatingApp.Entity.Reports;
import vn.iotstar.DatingApp.Service.IReportService;

@RestController
@RequestMapping("/report")
public class ReportController {
	@Autowired
	IReportService reportService;
	@PostMapping("/reportUser")
	public ResponseEntity<?> reportUser(@RequestBody Reports report)
	{
		reportService.save(report);
		return ResponseEntity.ok("Bao cao thanh cong");
	}
}
