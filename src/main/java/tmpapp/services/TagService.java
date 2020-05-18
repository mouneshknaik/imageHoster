package tmpapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tmpapp.model.Tag;
import tmpapp.repository.TagRepository;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public Tag getTagByName(String title) {
        return tagRepository.findTag(title);
    }

    public Tag createTag(Tag tag) {
        return tagRepository.createTag(tag);
    }
}
