package apschool.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import apschool.model.ReportRequest;
import apschool.model.SchoolData;
import apschool.utility.DataUtility;
import apschool.views.ExcelReport;

@Controller
public class DataController {
	
	DataUtility utility=new DataUtility();
	
	@RequestMapping(value= {"/","/home"}, method=RequestMethod.GET)
	public ModelAndView home() {
		ReportRequest reportRequest=new ReportRequest();
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("reportRequest",reportRequest);
		modelAndView.addObject("report",true);
		modelAndView.addObject("rep",false);
		modelAndView.addObject("msg","");
		modelAndView.setViewName("home");
		return modelAndView;
	}
	
	@RequestMapping(value = {"/","/home"}, method = RequestMethod.POST)
	public ModelAndView getHomData(ReportRequest reportRequest, @Valid BindingResult result) {
		if(result.hasErrors()) {
			ModelAndView modelAndView=new ModelAndView();
			modelAndView.addObject("reportRequest",reportRequest);
			modelAndView.addObject("report",true);
			modelAndView.addObject("rep",false);
			modelAndView.addObject("msg","Data Error");
			modelAndView.setViewName("home");
			return modelAndView;
		}
		return new ModelAndView((View) new ExcelReport(reportRequest.getFdate(),reportRequest.getTdate()));
		
	}
	
	@RequestMapping(value= {"/upload"}, method=RequestMethod.GET)
    public ModelAndView index() {
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("head",true);
		modelAndView.addObject("report",false);
		modelAndView.addObject("message","");
		modelAndView.setViewName("upload");
		return modelAndView;
    }
	
	@RequestMapping(value = {"/upload"}, method = RequestMethod.POST)
	public ModelAndView singleFileUpload(@RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

		if (file.isEmpty()) {
			ModelAndView modelAndView=new ModelAndView();
			modelAndView.addObject("message","Please select HTML file to upload");
			modelAndView.addObject("head",true);
			modelAndView.addObject("report",false);
			modelAndView.addObject("message","Please select a file");
			modelAndView.setViewName("upload");
			return modelAndView;
		}
		
		List<SchoolData> schoolDatas=utility.uploadFile(file);
		if(schoolDatas.size()==0) {
			ModelAndView modelAndView=new ModelAndView();
//			modelAndView.addObject("message","You successfully uploaded '" + file.getOriginalFilename() + "'");
			modelAndView.addObject("head",true);
			modelAndView.addObject("report",false);
			modelAndView.addObject("message","Upload Faild");
			modelAndView.setViewName("upload");
			return modelAndView;
		}
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("head",false);
		modelAndView.addObject("report",true);
		modelAndView.addObject("schoolDatas",schoolDatas);
		modelAndView.addObject("message","You successfully uploaded '" + file.getOriginalFilename() + "'");
		modelAndView.setViewName("upload");
		return modelAndView;
		}
}
