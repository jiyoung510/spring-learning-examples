package org.zerock.mapper;

import org.apache.ibatis.annotations.Select;

public interface TimeMapper {

	@Select("SELECT sysdate FROM dual")
	public String getTime(); //어노테이션을 사용하는 매퍼
	
	public String getTime2(); //xml과 함께 사용하는 매퍼
}
