package com.kenfei.admin.bpmn.service.impl;

import cn.hutool.core.io.file.PathUtil;
import com.kenfei.admin.bpmn.flowable.ActProcess;
import com.kenfei.admin.core.common.base.AbstractServiceImpl;
import com.kenfei.admin.bpmn.entity.ProcessRecordEntity;
import com.kenfei.admin.bpmn.repository.ProcessRecordRepository;
import com.kenfei.admin.bpmn.service.ProcessRecordService;
import org.flowable.engine.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * (TbProcessRecord)表服务实现类
 *
 * @author kenfei
 * @since 2021-10-28 16:34:39
 */
@Service("tbProcessRecordService")
public class ProcessRecordServiceImpl extends AbstractServiceImpl<ProcessRecordEntity, Integer> implements ProcessRecordService {
  private final ProcessRecordRepository tbProcessRecordRepository;
  @Resource private RepositoryService repositoryService;
  @Autowired private ActProcess actProcess;

  @Value("${spring.servlet.multipart.location}")
  private String filePath;
  /** 用来存放文件的目录 */
  private static  final String FOLDER = "process/";

  @Autowired
  public ProcessRecordServiceImpl(ProcessRecordRepository tbProcessRecordRepository) {
    super(tbProcessRecordRepository);
    this.tbProcessRecordRepository = tbProcessRecordRepository;
  }

  @Override
  public Page<ProcessRecordEntity> index(Pageable pageable) {
    return tbProcessRecordRepository.findAll(
        (root, cq, cb) -> {
          List<Predicate> list = new LinkedList<>();
          list.add(cb.equal(root.get("delFlag"), false));
          return cq.where(list.toArray(new Predicate[0])).getRestriction();
        },
        pageable);
  }

  @Override
  public ProcessRecordEntity deploy(Integer id) {
    ProcessRecordEntity entity = this.findValidById(id);
    Assert.notNull(entity, "该流程已被删除");

    // 部署流程
    InputStream xmlInputStream = new ByteArrayInputStream(entity.getXmlStr().getBytes());
    InputStream pngInputStream = new ByteArrayInputStream(entity.getSvgStr().getBytes());
    actProcess.deploy(entity.getName(), entity.getProcId(), xmlInputStream, pngInputStream);

    // 更新状态
    entity.setStatus(true);
    return this.update(entity);
  }


  public ProcessRecordEntity findValidById(Integer id) {
    ProcessRecordEntity entity = this.findById(id);
    return Optional.ofNullable(entity).filter(v -> !v.getDelFlag()).orElse(null);
  }

  @Override
  public ProcessRecordEntity save(ProcessRecordEntity entity) {
    entity.setVersion(1);
    return super.save(entity);
  }

  /**
   * 查询公共条件
   * @return 条件构造对象
   */
  private Specification<ProcessRecordEntity> findWhere(Integer id, Boolean delFlag) {
    return (root, cq, cb) -> {
      List<Predicate> list = new LinkedList<>();
      if (!Objects.isNull(id)) {
        list.add(cb.equal(root.get("id"), id));
      }
      if (!Objects.isNull(delFlag)) {
        list.add(cb.equal(root.get("delFlag"), delFlag));
      }

      return cq.where(list.toArray(new Predicate[0])).getRestriction();
    };
  }

  private String getFilePath() {
    String path = filePath + FOLDER;
    PathUtil.mkdir(new File(path).toPath());
    return path;
  }
}


