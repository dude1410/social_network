package javapro.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javapro.api.response.AllPersonDialogsResponse;
import javapro.api.response.DialogMessagesResponse;
import javapro.api.response.UnreadedCountResponse;
import javapro.config.Config;
import javapro.config.exception.BadRequestException;
import javapro.services.DialogsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Tag(name = "/api/v1/dialogs", description = "Диалоги")
public class DialogsController {

    private final DialogsService dialogsService;

    public DialogsController(DialogsService dialogsService) {
        this.dialogsService = dialogsService;
    }

    @GetMapping(value = "/api/v1/dialogs")
    @Operation(description = "Получение диалогов пользователя")
    public ResponseEntity<AllPersonDialogsResponse> getPersonDialogs(@RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                                                           @RequestParam(value = "itemPerPage", required = false, defaultValue = "20") Integer itemPerPage,
                                                           Principal principal) throws BadRequestException {
        if (principal == null) {
            throw new BadRequestException(Config.STRING_AUTH_ERROR);
        }
        return dialogsService.getAllPersonDialogs(principal.getName(), offset, itemPerPage);
    }

    @GetMapping(value = "/api/v1/dialogs/{id}/messages")
    @Operation(description = "Получение сообщений в диалоге")
    public ResponseEntity<DialogMessagesResponse> getMessagesFromDialogs(@PathVariable Integer id,
                                                         @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                                                         @RequestParam(value = "itemPerPage", required = false, defaultValue = "20") Integer itemPerPage) throws BadRequestException {
        return dialogsService.getDialogMessages(id, offset, itemPerPage);
    }

    @GetMapping(value = "/api/v1/dialogs/unreaded")
    @Operation(description = "Получение сообщений в диалоге")
    public ResponseEntity<UnreadedCountResponse> getMessagesFromDialogs(Principal principal) throws BadRequestException {
        if (principal == null) {
            throw new BadRequestException(Config.STRING_AUTH_ERROR);
        }
        return dialogsService.getUnreadCount(principal.getName());
    }
}
