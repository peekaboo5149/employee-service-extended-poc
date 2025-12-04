import { Button } from '@/components/ui/button'
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import {
  Field,
  FieldDescription,
  FieldGroup,
  FieldLabel,
} from '@/components/ui/field'
import { Input } from '@/components/ui/input'
import { cn } from '@/lib/utils'
import { useAuthToggle } from '../context'
import { PasswordInput } from './form-input'

function RegisterView({ className, ...props }: React.ComponentProps<'div'>) {
  const { toggle } = useAuthToggle()
  return (
    <div className={cn('flex flex-col gap-6', className)} {...props}>
      <Card>
        <CardHeader>
          <CardTitle>Register a new account</CardTitle>
          <CardDescription>
            Enter your email below to create a new account
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form>
            <FieldGroup>
              <Field>
                <FieldLabel htmlFor='email'>Email</FieldLabel>
                <Input
                  id='email'
                  type='email'
                  placeholder='m@example.com'
                  required
                />
              </Field>
              <PasswordInput />
              <PasswordInput label='Confirm Password' />
              <Field>
                <Button type='submit'>Register</Button>
                <FieldDescription className='text-center'>
                  Already have an account?{' '}
                  <a onClick={toggle} href='#'>
                    Sign in
                  </a>
                </FieldDescription>
              </Field>
            </FieldGroup>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default RegisterView
