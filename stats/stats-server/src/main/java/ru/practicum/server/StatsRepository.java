package ru.practicum.server;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.model.Hit;
import ru.practicum.server.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {

    @Query("""
            Select new ru.practicum.server.model.Stat(h.app, h.uri, count(distinct h.ip))
            from Hit h
            WHERE h.timestamp between ?1 and ?2
            and h.uri in (?3)
            group by h.app, h.uri
            order by count(distinct h.ip) desc
            """)
    List<Stat> findAllWithUrisUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("""
            Select new ru.practicum.server.model.Stat(h.app, h.uri, count(h.ip))
            from Hit h
            WHERE h.timestamp between ?1 and ?2
            and h.uri in (?3)
            group by h.app, h.uri
            order by count(h.ip) desc
            """)
    List<Stat> findAllWithUrisNotUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("""
            Select new ru.practicum.server.model.Stat(h.app, h.uri, count(distinct h.ip))
            from Hit h
            WHERE h.timestamp between ?1 and ?2
            group by h.app, h.uri
            order by count(distinct h.ip) desc
            """)
    List<Stat> findAllWithoutUrisUnique(LocalDateTime start, LocalDateTime end);

    @Query("""
            Select new ru.practicum.server.model.Stat(h.app, h.uri, count((h.ip)))
            from Hit h
            WHERE h.timestamp between ?1 and ?2
            group by h.app, h.uri
            order by count(h.ip) desc
            """)
    List<Stat> findAllWithoutUrisNotUnique(LocalDateTime start, LocalDateTime end);
}
