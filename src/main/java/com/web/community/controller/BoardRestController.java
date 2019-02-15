package com.web.community.controller;

import com.web.community.domain.Board;
import com.web.community.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RepositoryRestController
//@RequestMapping("/api/boards")
public class BoardRestController {
    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/boards")
    public @ResponseBody Resources<Board> getBoards(@PageableDefault Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);
        PagedResources.PageMetadata pageMetadata = new PagedResources.PageMetadata(pageable.getPageSize(), boards.getNumber(), boards.getTotalElements());
        PagedResources<Board> resources = new PagedResources<>(boards.getContent(), pageMetadata);
        resources.add(linkTo(methodOn(BoardRestController.class).getBoards(pageable)).withSelfRel());
        return resources;
    }

//    @PostMapping
//    public ResponseEntity<?> postBoard(@RequestBody Board board) {
//        board.setCreatedDateNow();
//        boardRepository.save(board);
//        return new ResponseEntity<>("{}", HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<?> putBoard(@PathVariable("id") Long id, @RequestBody Board board) {
//        Board persistBoard = boardRepository.getOne(id);
//        persistBoard.update(board);
//        boardRepository.save(persistBoard);
//        return new ResponseEntity<>("{}", HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteBoard(@PathVariable("id") Long id) {
//        boardRepository.deleteById(id);
//        return new ResponseEntity<>("{}", HttpStatus.OK);
//    }
}
