package br.insper.aps.produto;

import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface ProdutoRepository extends MongoRepository<Produto, String> {
}
