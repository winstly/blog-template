package cn.yanshisan.blog.interaction.interfaces.api;

import cn.yanshisan.blog.interaction.application.InteractionApplicationService;
import cn.yanshisan.blog.interaction.interfaces.dto.MessageCreateDTO;
import cn.yanshisan.blog.interaction.interfaces.dto.InteractionVO;
import cn.yanshisan.blog.shared.api.PageResult;
import cn.yanshisan.blog.shared.api.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final InteractionApplicationService interactionApplicationService;

    @PostMapping
    public R<InteractionVO> create(@RequestBody @Valid MessageCreateDTO dto) {
        InteractionVO vo;
        if (dto.getParentId() != null) {
            vo = interactionApplicationService.createReply(
                    dto.getParentId(), "message",
                    dto.getUserName(), dto.getUserAvatarUrl(), dto.getRemark(), dto.getExtMeta());
        } else {
            vo = interactionApplicationService.createInteraction(
                    "site", "site", "message",
                    dto.getUserName(), dto.getUserAvatarUrl(), dto.getRemark(), dto.getExtMeta());
        }
        return R.ok(vo);
    }

    @GetMapping
    public R<PageResult<InteractionVO>> list(@RequestParam(required = false) String status,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "20") int pageSize) {
        Integer displayStatus = null;
        if (status != null && !status.isBlank()) {
            displayStatus = "approved".equals(status) ? 1 : 0;
        }
        PageResult<InteractionVO> result = interactionApplicationService.listMessages(displayStatus, page, pageSize);
        return R.ok(result);
    }

    @PutMapping("/{id}/approve")
    public R<Void> approve(@PathVariable Long id) {
        interactionApplicationService.approve(id);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        interactionApplicationService.deleteInteraction(id);
        return R.ok();
    }

    @PutMapping("/batch-approve")
    public R<Void> batchApprove(@RequestBody List<Long> ids) {
        interactionApplicationService.batchApprove(ids);
        return R.ok();
    }

    @DeleteMapping("/batch")
    public R<Void> batchDelete(@RequestBody List<Long> ids) {
        interactionApplicationService.batchDelete(ids);
        return R.ok();
    }
}
