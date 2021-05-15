package com.example.batch.reader;

import com.example.batch.reader.option.QuerydslNoOffsetOptions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManagerFactory;
import java.util.function.Function;

public class QuerydslNoOffsetPagingItemReader<T> extends QuerydslPagingItemReader<T> {

    private QuerydslNoOffsetOptions<T> options;

    private QuerydslNoOffsetPagingItemReader() {
        super();
        setName(ClassUtils.getShortName(QuerydslNoOffsetPagingItemReader.class));
    }

    public QuerydslNoOffsetPagingItemReader(EntityManagerFactory entityManagerFactory,
                                            int pageSize,
                                            QuerydslNoOffsetOptions<T> options,
                                            Function<JPAQueryFactory, JPAQuery<T>> queryFunction) {
        super(entityManagerFactory, pageSize, queryFunction);
        setName(ClassUtils.getShortName(QuerydslNoOffsetPagingItemReader.class));
        this.options = options;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doReadPage() {

        clearIfTransacted();

        JPAQuery<T> query = createQuery().limit(getPageSize());

        initResults();

        fetchQuery(query);

        resetCurrentIdIfNotLastPage();
    }

    /**
     * JPQL 만드는 로직
     * NoOffset 은 여기서 getPage 만 들어감.
     * @return
     */
    @Override
    protected JPAQuery<T> createQuery() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        JPAQuery<T> query = function.apply(queryFactory);
        options.initKeys(query, getPage()); // 제일 첫번째 페이징시 시작해야할 ID 찾기. 이걸 만약 안찾으면 offset 구하기 위해 resource 들어감.
        return options.createQuery(query, getPage());
    }

    /**
     * 마지막 읽은 데이터를 초기화 시켜줌.
     */
    private void resetCurrentIdIfNotLastPage() {
        if (isNotEmptyResults()) {
            options.resetCurrentId(getLastItem());
        }
    }

    // 조회결과가 Empty이면 results에 null이 담긴다
    private boolean isNotEmptyResults() {
        return !CollectionUtils.isEmpty(results) && results.get(0) != null;
    }

    private T getLastItem() {
        return results.get(results.size() - 1);
    }
}