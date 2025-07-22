package com.antmen.antwork.domain.board.service;





















@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final AlertService alertService;

    @Transactional
    public void commentWrite(Long userId, Long boardId, CommentRequestDto commentRequestDto) {
        User user = userRepository.findByUserId(userId);
        Board board = boardRepository.findById(boardId).get();
        if (board.getBoardIsDeleted() == true) {
            throw new IllegalArgumentException("삭제된 게시글 입니다.");
        }
        commentRepository.save(commentMapper.toEntity(userId, boardId, commentRequestDto));

        if ((board.getBoardType().equals("customer") || board.getBoardType().equals("manager"))
                && board.getIsPinned() == false && user.getUserRole() == UserRole.ADMIN) {
            board.setBoardStatus(BoardStatus.InProgress);

            if (board.getBoardType().equals("customer")) {
                alertService.sendAlert(board.getBoardUserId(), AlertTrigger.COMMENT_ON_POST_FOR_CUSTOMER, boardId);
            } else {
                alertService.sendAlert(board.getBoardUserId(), AlertTrigger.COMMENT_ON_POST_FOR_MANAGER, boardId);
            }

        }

    }

    @Transactional
    public void commentUpdate(Long userId, Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if (comment.getCommentIsDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제된 댓글입니다.");
        }

        if (!comment.getCommentUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 댓글만 수정 가능합니다.");
        }

        comment.setCommentContent(commentRequestDto.getContent());
        comment.setCommentModifiedAt(LocalDateTime.now());
    }

    @Transactional
    public void commentDelete(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));

        if (comment.getCommentIsDeleted()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "이미 삭제된 댓글입니다.");
        }

        User user = userRepository.findByUserId(userId);

        if (!comment.getCommentUserId().equals(userId)) {
            if (user.getUserRole() == UserRole.ADMIN) {
                comment.setCommentContent("관리자가 삭제한 댓글입니다.");
                comment.setCommentModifiedAt(LocalDateTime.now());
                return;
            }
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인이 작성한 댓글만 삭제 가능합니다.");
        }

        comment.setCommentIsDeleted(true);
    }
}