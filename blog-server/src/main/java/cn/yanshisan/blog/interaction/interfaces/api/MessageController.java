package cn.yanshisan.blog.interaction.interfaces.api;

import cn.yanshisan.blog.interaction.application.InteractionApplicationService;
import cn.yanshisan.blog.interaction.interfaces.dto.MessageCreateDTO;
import cn.yanshisan.blog.interaction.interfaces.dto.InteractionVO;
import cn.yanshisan.blog.shared.api.PageResult;
import cn.yanshisan.blog.shared.api.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public R<PageResult<InteractionVO>> list(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "20") int pageSize) {
        PageResult<InteractionVO> result = interactionApplicationService.listByTarget(
                "site", "site", "message", page, pageSize);
        return R.ok(result);
    }
}
