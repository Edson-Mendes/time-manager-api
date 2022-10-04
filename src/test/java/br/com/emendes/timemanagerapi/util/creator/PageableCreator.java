package br.com.emendes.timemanagerapi.util.creator;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableCreator {

  public static Pageable activityDefaultPageable(){
    Sort.Order orderByStatus = new Sort.Order(Sort.Direction.ASC, "status");
    Sort.Order orderByCreatedAt = new Sort.Order(Sort.Direction.DESC, "createdAt");
    return PageRequest.of(0, 10, Sort.by(orderByStatus, orderByCreatedAt));
  }

}
