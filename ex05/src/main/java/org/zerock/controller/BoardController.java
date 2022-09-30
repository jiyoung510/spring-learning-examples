package org.zerock.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
import org.zerock.service.BoardService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@RequestMapping("/board/*")
@AllArgsConstructor
public class BoardController {

	private BoardService service;
	
//	@GetMapping("/list") // http://localhost/board/list
//	public void list(Model model) {
//		log.info("list");
//		model.addAttribute("list", service.getList());
//				
//	}
	@GetMapping("/list")
	public void list(Criteria cri, Model model) {
		log.info("list : " + cri);
		model.addAttribute("list", service.getList(cri));
//		model.addAttribute("pageMaker", new PageDTO(cri, 123));
		
		int total = service.getTotal(cri);
		
		log.info("total : " + total);
		
		model.addAttribute("pageMaker", new PageDTO(cri, total));
	}
	
	@GetMapping("/register")
	public void register() {
		// http://localhost/board/register의 요청이 왔을 때
		// board/register.jsp로 넘겨주는 역할만 진행함으로 별도의 코드는 필요없다. 
	}
	
	@PostMapping("/register") // http://localhost/board/register
	public String register(BoardVO board, RedirectAttributes rttr) {
		//String으로 리턴 타입을 지정하고, RedirectAttributes 파라미터로 지정
		
		log.info("=========================================");
		
		log.info("register : " + board);
		
		if(board.getAttachList() != null) {
			
			board.getAttachList().forEach(attach -> log.info(attach));
			
		}
		
		log.info("=========================================");
		
		service.register(board);  // 등록 작업
		rttr.addFlashAttribute("result", board.getBno()); //새롭게 등록된 게시물번호 전달
		
		return "redirect:/board/list"; // 다시 목록으로 되돌아감
		// redirect:/ 스프링 MVC가 response.sendRedirect()를 처리한다.
	}
	
	@GetMapping({"/get","/modify"})  //게시물을 번호를 전달 받아 해당하는 게시물을 출력
	//추가 get과 modify를 이중 구현 한다. -> 수정 추가 분(배열로 구현하고 /modify를 추가
	public void get(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri ,Model model) {
		
		log.info("/get or /modify");
		model.addAttribute("board", service.get(bno));
	}
	
	 @PostMapping("/modify")
	 public String modify(BoardVO board, Criteria cri, RedirectAttributes rttr) {
	 log.info("modify:" + board);
	
	 if (service.modify(board)) {
	 rttr.addFlashAttribute("result", "success");
	 }
	 
	 return "redirect:/board/list" + cri.getListLink();
	 }
	 
	 @PostMapping("/remove")
	 public String remove(@RequestParam("bno") Long bno, Criteria cri, RedirectAttributes rttr)
	 {
	
	 log.info("remove..." + bno);
	 
	 if (service.remove(bno)) {
	 rttr.addFlashAttribute("result", "success");
	 }
	 
	 return "redirect:/board/list" + cri.getListLink();
	 }
	 
	 @GetMapping(value = "/getAttachList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	 @ResponseBody
	 public ResponseEntity<List<BoardAttachVO>> getAttachList(Long bno) {
		 log.info("getAttachList " + bno);
		 
		 return new ResponseEntity<>(service.getAttachList(bno), HttpStatus.OK);
	 }
}
