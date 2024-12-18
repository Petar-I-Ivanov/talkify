package bg.uniplovdiv.talkify.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

public abstract class PagedRepresentationAssembler<E, EM extends RepresentationModel<EM>>
    extends RepresentationModelAssemblerSupport<E, EM> {

  @Autowired private PagedResourcesAssembler<E> pagedResourceAssembler;

  protected PagedRepresentationAssembler(Class<?> controllerClass, Class<EM> resourceType) {
    super(controllerClass, resourceType);
  }

  @SuppressWarnings("unchecked")
  public PagedModel<EM> toPagedModel(Page<E> entities) {
    return entities.isEmpty()
        ? (PagedModel<EM>) pagedResourceAssembler.toEmptyModel(entities, getResourceType())
        : pagedResourceAssembler.toModel(entities, this);
  }
}
