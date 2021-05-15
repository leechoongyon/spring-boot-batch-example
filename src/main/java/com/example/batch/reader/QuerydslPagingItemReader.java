package com.example.batch.reader;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;


/**
 * QuerydslPagingItemReader
 * @param <T>
 */

public class QuerydslPagingItemReader<T> extends AbstractPagingItemReader<T> {

    protected final Map<String, Object> preoperties = new HashMap<>();
    protected EntityManagerFactory emf;
    protected EntityManager em;
    protected Function<JPAQueryFactory, JPAQuery<T>> function;
    protected boolean transacted = true;    //default value

    protected QuerydslPagingItemReader() {
        setName(ClassUtils.getShortName(QuerydslPagingItemReader.class));
    }

    public QuerydslPagingItemReader(EntityManagerFactory emf,
                                    int pageSize,
                                    Function<JPAQueryFactory, JPAQuery<T>> function) {
        this();
        this.emf = emf;
        this.function = function;
        setPageSize(pageSize);
    }

    public void setTransacted(boolean transacted) {
        this.transacted = transacted;
    }

    @Override
    protected void doOpen() throws Exception {
        super.doOpen();

        em = emf.createEntityManager(preoperties);
        if (em == null) {
            throw new DataAccessResourceFailureException("Unable to obtain an EntityManager");
        }
    }



    /**
     *  영속성컨텍스트 초기화 하지 않으면 대용량 쌓여서 OOM 발생 가능성 있음.
     */
    protected void clearIfTransacted() {
        if (transacted) {
            em.clear();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doReadPage() {

        clearIfTransacted();

        /**
         * 이게 핵심 로직임. 기존 JpaPagingItemReader 에다가 뒤에 offset, limit 을 추가.
         */
        JPAQuery<T> query = createQuery()
                .offset(getPage() * getPageSize())
                .limit(getPageSize());

        initResults();

        fetchQuery(query);
    }

    protected JPAQuery<T> createQuery() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return function.apply(queryFactory);
    }

    /**
     * 메모리 초기화
     */
    protected void initResults() {
        if (CollectionUtils.isEmpty(results)) {
            results = new CopyOnWriteArrayList<>();
        } else {
            results.clear();
        }
    }

    protected void fetchQuery(JPAQuery<T> query) {
        if (!transacted) {
            List<T> queryResult = query.fetch();
            for (T entity : queryResult) {
                em.detach(entity);
                results.add(entity);
            }
        } else {
            results.addAll(query.fetch());
        }
    }

    /**
     * 효율적인 page 이동법이 있으면 구현.
     * @param index
     */
    @Override
    protected void doJumpToPage(int index) {
    }

    @Override
    protected void doClose() throws Exception {
        em.close();
        super.doClose();
    }
}