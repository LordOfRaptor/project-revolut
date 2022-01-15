package fr.miage.revolut.services.assembler;

import fr.miage.revolut.controllers.CardsController;
import fr.miage.revolut.controllers.TransactionsController;
import fr.miage.revolut.dto.view.CardView;
import fr.miage.revolut.dto.view.TransactionView;
import fr.miage.revolut.entities.Card;
import fr.miage.revolut.entities.Transaction;
import fr.miage.revolut.mapper.TransactionsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class TransactionAssembler implements RepresentationModelAssembler<Transaction, EntityModel<TransactionView>> {

    private final TransactionsMapper mapper;

    @Override
    public EntityModel<TransactionView> toModel(Transaction entity) {
        return null;
    }

    public EntityModel<TransactionView> toModelWithAccount(Transaction entity,String uuid){
        return EntityModel.of(mapper.toDto(entity),
                linkTo(methodOn(TransactionsController.class)
                        .getTransactions(uuid,null)).withRel("collections"),
                linkTo(methodOn(TransactionsController.class)
                        .getTransaction(uuid, entity.getUuid())).withSelfRel()
                );
    }

    @Override
    public CollectionModel<EntityModel<TransactionView>> toCollectionModel(Iterable<? extends Transaction> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }

    public CollectionModel<EntityModel<TransactionView>> toCollectionModelWithAccount(Iterable<? extends Transaction> entities,String uuid) {
        List<EntityModel<TransactionView>> transactionModel = StreamSupport
                .stream(entities.spliterator(), false)
                .map(i -> toModelWithAccount(i,uuid))
                .toList();
        return CollectionModel.of(transactionModel,
                linkTo(methodOn(TransactionsController.class)
                        .getTransactions(uuid,null)).withSelfRel());
    }
}
