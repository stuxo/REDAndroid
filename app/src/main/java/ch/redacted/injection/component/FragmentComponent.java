package ch.redacted.injection.component;

/**
 * Created by sxo on 27/12/16.
 */

import dagger.Subcomponent;
import ch.redacted.injection.PerFragment;
import ch.redacted.injection.module.FragmentModule;

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {
}