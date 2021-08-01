package javapro.services;

import javapro.api.response.*;
import javapro.config.exception.BadRequestException;
import javapro.model.Dialog;
import javapro.model.DialogMessage;
import javapro.model.Person;
import javapro.model.dto.DialogMessageDTO;
import javapro.repository.DialogRepository;
import javapro.repository.PersonRepository;
import javapro.util.Time;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DialogsService {

    private final DialogRepository dialogRepository;
    private final PersonRepository personRepository;

    public DialogsService(DialogRepository dialogRepository, PersonRepository personRepository) {
        this.dialogRepository = dialogRepository;
        this.personRepository = personRepository;
    }

    public ResponseEntity<AllPersonDialogsResponse> getAllPersonDialogs(String personEmail, Integer offset, Integer perPage) {
        Person person = personRepository.findByEmail(personEmail);
        int pageNumber = offset / perPage;
        AllPersonDialogsResponse allPersonDialogsResponse = new AllPersonDialogsResponse();
        List<DialogData> dialogData = new ArrayList<>();
        Pageable pageWithDialogs = PageRequest.of(pageNumber, perPage);
        List<Dialog> allUserDialog = dialogRepository.findAllPersonDialogs(pageWithDialogs, person.getId());

        allPersonDialogsResponse.setError("string");
        allPersonDialogsResponse.setTimestamp(Time.getTime());
        allPersonDialogsResponse.setTotal(allUserDialog.size());
        allPersonDialogsResponse.setOffset(offset);
        allPersonDialogsResponse.setPerPage(perPage);

        if (allUserDialog.size() > 0) {
            for (Dialog dialog : allUserDialog) {
                List<DialogMessage> dialogMessages = dialog.getDialogMessageList();
                DialogMessage lastDialogMessage = dialogMessages.get(dialogMessages.size() - 1);
                dialogData.add(new DialogData(dialog.getId(), 0,
                        new DialogMessageDTO(lastDialogMessage.getId(),
                                lastDialogMessage.getTime().getTime(),
                                lastDialogMessage.getAuthorId().getId(),
                                lastDialogMessage.getRecipientId().getId(),
                                lastDialogMessage.getMessageText(),
                                lastDialogMessage.getReadStatus())));
            }
        }
        allPersonDialogsResponse.setData(dialogData);
        return new ResponseEntity<>(allPersonDialogsResponse, HttpStatus.OK);
    }

    public ResponseEntity<DialogMessagesResponse> getDialogMessages(Integer dialogId, Integer offset, Integer perPage) throws BadRequestException {
        DialogMessagesResponse dialogMessagesResponse = new DialogMessagesResponse();
        List<DialogMessageData> dialogMessageDataList = new ArrayList<>();
        Dialog dialog = dialogRepository.findById(dialogId).orElseThrow(() -> new BadRequestException("dialog not found"));
        List<DialogMessage> allDialogMessages = dialog.getDialogMessageList();
        List <DialogMessage> dialogMessagePage = allDialogMessages.stream()
                                                                  .skip(offset)
                                                                  .limit(perPage)
                                                                  .collect(Collectors.toList());
        dialogMessagesResponse.setError("string");
        dialogMessagesResponse.setTimestamp(Time.getTime());
        dialogMessagesResponse.setTotal(allDialogMessages.size());
        dialogMessagesResponse.setOffset(offset);
        dialogMessagesResponse.setPerPage(perPage);
        if (dialogMessagePage.size() > 0) {
            for (DialogMessage dialogMessage : dialogMessagePage) {
                RecipientData recipientData = new RecipientData();
                recipientData.setId(dialogMessage.getRecipientId().getId());
                recipientData.setFirstName(dialogMessage.getRecipientId().getFirstName());
                recipientData.setLastName(dialogMessage.getRecipientId().getLastName());
                recipientData.setLastOnlineTime(dialogMessage.getRecipientId().getLastOnlineTime().getTime());
                dialogMessageDataList.add(new DialogMessageData(dialogMessage.getId(),
                                                                dialogMessage.getAuthorId().getId(),
                                                                recipientData,
                                                                dialogMessage.getMessageText(),
                                                                dialogMessage.getReadStatus()));
            }
        }
        dialogMessagesResponse.setData(dialogMessageDataList);
        return new ResponseEntity<>(dialogMessagesResponse, HttpStatus.OK);
    }
}
