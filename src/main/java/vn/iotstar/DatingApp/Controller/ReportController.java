package vn.iotstar.DatingApp.Controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
	
	/**
	 * Báo cáo người dùng
	 * 
	 * 
	 */
	@PostMapping("/reportUser")
	public ResponseEntity<?> reportUser(@RequestBody Reports report)
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		report.setReportAt(new Date());
		reportService.save(report);
		return ResponseEntity.ok(null);
	}
}
