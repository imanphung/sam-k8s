package com.samhello.sam.web.rest;

import com.samhello.sam.domain.Room;
import com.samhello.sam.repository.RoomRepository;
import com.samhello.sam.service.RoomService;
import com.samhello.sam.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.samhello.sam.domain.Room}.
 */
@RestController
@RequestMapping("/api")
public class RoomResource {

    private final Logger log = LoggerFactory.getLogger(RoomResource.class);

    private static final String ENTITY_NAME = "samRoom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoomService roomService;

    private final RoomRepository roomRepository;

    public RoomResource(RoomService roomService, RoomRepository roomRepository) {
        this.roomService = roomService;
        this.roomRepository = roomRepository;
    }

    /**
     * {@code POST  /rooms} : Create a new room.
     *
     * @param room the room to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new room, or with status {@code 400 (Bad Request)} if the room has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rooms")
    public ResponseEntity<Room> createRoom(@Valid @RequestBody Room room) throws URISyntaxException {
        log.debug("REST request to save Room : {}", room);
        if (room.getId() != null) {
            throw new BadRequestAlertException("A new room cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Room result = roomService.save(room);
        return ResponseEntity
            .created(new URI("/api/rooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rooms/:id} : Updates an existing room.
     *
     * @param id the id of the room to save.
     * @param room the room to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated room,
     * or with status {@code 400 (Bad Request)} if the room is not valid,
     * or with status {@code 500 (Internal Server Error)} if the room couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rooms/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Room room)
        throws URISyntaxException {
        log.debug("REST request to update Room : {}, {}", id, room);
        if (room.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, room.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Room result = roomService.update(room);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, room.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rooms/:id} : Partial updates given fields of an existing room, field will ignore if it is null
     *
     * @param id the id of the room to save.
     * @param room the room to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated room,
     * or with status {@code 400 (Bad Request)} if the room is not valid,
     * or with status {@code 404 (Not Found)} if the room is not found,
     * or with status {@code 500 (Internal Server Error)} if the room couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rooms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Room> partialUpdateRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Room room
    ) throws URISyntaxException {
        log.debug("REST request to partial update Room partially : {}, {}", id, room);
        if (room.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, room.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Room> result = roomService.partialUpdate(room);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, room.getId().toString())
        );
    }

    /**
     * {@code GET  /rooms} : get all the rooms.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rooms in body.
     */
    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> getAllRooms(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Rooms");
        Page<Room> page = roomService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rooms/:id} : get the "id" room.
     *
     * @param id the id of the room to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the room, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rooms/{id}")
    public ResponseEntity<Room> getRoom(@PathVariable Long id) {
        log.debug("REST request to get Room : {}", id);
        Optional<Room> room = roomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(room);
    }

    /**
     * {@code DELETE  /rooms/:id} : delete the "id" room.
     *
     * @param id the id of the room to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rooms/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        log.debug("REST request to delete Room : {}", id);
        roomService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
