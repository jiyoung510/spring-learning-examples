package org.zerock.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.mapper.BoardAttachMapper;
import org.zerock.mapper.BoardMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service //해당 클래스를 루트 컨테이너 빈객체로 생성(서비스 레이어, 내부에서 자바 로직을 처리)
public class BoardServiceImpl implements BoardService {
	
	@Setter(onMethod_ = @Autowired)
	private BoardMapper mapper;
	
	@Setter(onMethod_ = @Autowired)
	private BoardAttachMapper attachMapper;
	
	@Transactional
	@Override
	public void register(BoardVO board) {
		
		log.info("register............." + board);
		
		mapper.insertSelectKey(board);
		
		if(board.getAttachList() == null || board.getAttachList().size() <= 0) {
			return;
		}
		
		board.getAttachList().forEach(attach -> {
			attach.setBno(board.getBno());
			attachMapper.insert(attach);
		});
	} 

	@Override
	public BoardVO get(Long bno) {
		
		log.info("Get.............." + bno) ;
		return mapper.read(bno);
	}

	@Override
	public boolean modify(BoardVO board) {
		
		log.info("modify............" + board); 
		return mapper.update(board) == 1 ;
	}

	@Override
	public boolean remove(Long bno) {
		
		log.info("remove............" + bno); 
		return mapper.delete(bno) == 1 ;
	}

//	@Override
//	public List<BoardVO> getList() {
//		
//		log.info("getList.................");
//		
//		return mapper.getList();
//	}
	
	@Override
	public List<BoardVO> getList(Criteria cri) {

		log.info("get List with criteria : " + cri);
		
		return mapper.getListWithPaging(cri);
	}

	@Override
	public int getTotal(Criteria cri) {
		log.info("get total count");
		return mapper.getTotalCount(cri);
	}

	@Override
	public List<BoardAttachVO> getAttachList(Long bno) {
		log.info("get Attach list by bno" + bno);
		
		return attachMapper.findByBno(bno);
	}
	

}
