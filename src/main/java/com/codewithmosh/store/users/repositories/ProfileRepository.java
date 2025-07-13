package com.codewithmosh.store.users.repositories;

import com.codewithmosh.store.users.entities.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}